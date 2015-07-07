package server

import java.net.InetSocketAddress
import java.util.concurrent.ExecutorService
import java.nio.ByteBuffer
import java.security.{ KeyStore, Security }
import javax.net.ssl.{ TrustManagerFactory, KeyManagerFactory, SSLContext }

import org.http4s._
import org.http4s.blaze.pipeline._
import org.http4s.blaze.pipeline.stages._
import org.http4s.blaze.channel._
import org.http4s.blaze.channel.nio2._
import org.http4s.server.blaze._
import org.http4s.server.middleware._
import org.http4s.server._

import scalaz.syntax.std.option._
import scalaz.concurrent._

class CustomServer private (address: InetSocketAddress, pf: BufferPipelineBuilder, group: NIO2SocketServerGroup) extends Server {
  val channel = group.bind(address, pf).get
  
  override def shutdown() = Task.delay {
    channel.close()
    group.closeGroup()
    this
  }
  
  override def onShutdown(f: => Unit) = {
    channel.addShutdownHook(() => f)
    this
  }
}

object CustomServer {
  def newBuilder = 
    new ServerBuilder()
  
  class ServerBuilder() {
    def result(): Server = {
      val pf = 
        if (useWebSocket) (conn: SocketConnection) => LeafBuilder(new Http1ServerStage(service, conn.some, exec) with WebSocketSupport)
        else (conn: SocketConnection) => LeafBuilder(new Http1ServerStage(service, conn.some, exec))
        
      new CustomServer(address, pf, serverGroup)
    }
    
    private var address: InetSocketAddress = new InetSocketAddress("localhost", 8080)
    private var service: HttpService = Service.empty[Request, Response]
    private var exec: ExecutorService = Strategy.DefaultExecutorService
    private var serverGroup: NIO2SocketServerGroup = NIO2SocketServerGroup()
    private var useWebSocket: Boolean = false
    private var prepend: Option[Seq[MidStage[ByteBuffer, ByteBuffer]]] = None
    
    def bindAddress(address: InetSocketAddress) = {
      this.address = address
      this
    }
    
    def bindLocal(port: Int = 8080) = {
      address = new InetSocketAddress("localhost", port)
      this
    }
    
    def addService(service: HttpService, path: String = "/") = {
      val svc = if (path == "/" || path == "") service else URITranslation.translateRoot(path)(service)
      if (svc.run ne Service.empty.run)
        this.service = if (this.service.run eq Service.empty.run) svc else this.service orElse svc
      this
    }
    
    def withExec(exec: ExecutorService) = {
      this.exec = exec
      this
    }
    
    def withServerGroup(serverGroup: NIO2SocketServerGroup) = {
      this.serverGroup = serverGroup
      this
    }
    
    def withWebSocker() = {
      this.useWebSocket = true
      this
    }
    
    def withSSL(bits: SSLSupport.SSLBits) = {
      val ks = KeyStore.getInstance("JKS")
      val ksStream = new java.io.FileInputStream(bits.keyStore.path)
      try ks.load(ksStream, bits.keyStore.password.toCharArray)
      finally ksStream.close()      
      
      val tmf = bits.trustStore.map { auth =>
        val ks = KeyStore.getInstance("JKS")
        val ksStream = new java.io.FileInputStream(bits.keyStore.path)
        
        try ks.load(ksStream, auth.password.toCharArray())
        finally ksStream.close()
        
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
        tmf.init(ks)
        tmf.getTrustManagers
      }       

      val kmf = KeyManagerFactory.getInstance(
                  Option(Security.getProperty("ssl.KeyManagerFactory.algorithm"))
                    .getOrElse(KeyManagerFactory.getDefaultAlgorithm))

      kmf.init(ks, bits.keyManagerPassword.toCharArray)

      val context = SSLContext.getInstance(bits.protocol)
      context.init(kmf.getKeyManagers(), tmf.orNull, null)    
      
      val engine = context.createSSLEngine()
      engine.setUseClientMode(false)
      engine.setNeedClientAuth(bits.clientAuth)
      
      val stage = new SSLStage(engine)
      prepend = prepend.map(_ :+ stage) orElse Seq(stage).some
      
      this
    }
  }
}

object ServerApp extends App {
  import org.http4s.dsl._
  import org.http4s.headers._
  import org.http4s.websocket.WebsocketBits.Text
  import org.http4s.server.websocket.WS
  import scalaz.stream._
  import scala.concurrent.duration._
  
  implicit val sch = Strategy.DefaultTimeoutScheduler
  
  val svc = HttpService {
    case r @ GET -> Root / "ping" =>
      Ok("pong")
    case r @ GET -> Root / "test" =>
      val p = (Process.constant("test").take(5) zip time.awakeEvery(100 millis)).map(_._1)
      Ok(p).putHeaders(`Transfer-Encoding`(TransferCoding.chunked))
    case r @ GET -> Root / "websocket" =>
      val src = time.awakeEvery(100 millis).zipWithIndex.map({case (_, i) => Text(s"delay -> $i")})
      WS(Exchange(src, Process.halt))
  }
  
  CustomServer.newBuilder.bindLocal().addService(svc).withWebSocker().result().awaitShutdown()
}