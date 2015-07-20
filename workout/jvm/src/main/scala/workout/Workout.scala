package workout

import server._
import resource._

object WorkoutApp extends App {
  val svc = CacheRoute.noCache(new java.io.File("js")).svc.contramap { (req: org.http4s.Request) =>
    Console.println(s"${req.method}: ${req.pathInfo}")    
    req  
  }
  
  CustomServer.newBuilder.bindLocal().addService(svc).result().awaitShutdown()
}