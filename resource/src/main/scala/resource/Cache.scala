package resource

import scala.collection.concurrent

import scalaz.concurrent._
import scalaz.stream._

import scalaz.syntax.std.option._

import org.http4s._
import org.http4s.headers._
import org.http4s.dsl._

trait Cache {
  def getResource(directory: String, name: String, req: Request): Task[Response]
}

class NoCache(base: java.io.File) extends Cache {
  require(base.exists(), "base directory does not exist: " + base.getPath)
  
  def getResource(directory: String, name: String, req: Request): Task[Response] = {
    Task.fork {
      val file = new java.io.File( base, s"$directory/${name.stripPrefix("/")}" )
      val fin = (if (file.exists()) new java.io.FileInputStream(file).some else None) orElse Option(getClass.getResourceAsStream(s"$directory/${name.stripPrefix("/")}"))
      fin map { is =>
        val src = Process.constant(8 * 1024).toSource.through(io.chunkR(is).mapOut(_.toArray)).onComplete(Process.eval_(Task.delay(is.close())))
        Ok(src)
          .putHeaders( `Content-Type`(name.split("\\.").lastOption.flatMap(MediaType.forExtension(_)).getOrElse(MediaType.`application/octet-stream`)) )
          .putHeaders( `Transfer-Encoding`(TransferCoding.chunked) )
          .putHeaders( `Cache-Control`(CacheDirective.`no-cache`(), CacheDirective.`no-store`, CacheDirective.`must-revalidate`) )        
      } getOrElse NotFound(s"file does not exist: ${file.getPath}")
    }
  }
}

object NoCache {
  def apply(): Cache = 
    new NoCache(new java.io.File("src/main/resources"))
}

class ResourceCache extends Cache {
  import ResourceCache._
  
  val start = DateTime.now
  private val cache = concurrent.TrieMap.empty[String, Array[Byte]]
  
  def getResource(directory: String, name: String, req: Request): Task[Response] = {
    
    req.headers.get(`If-Modified-Since`).flatMap({ h =>
      val expired = h.date.compare(start) < 0
      if (expired) None
      else NotModified().some
    }).getOrElse(getResourceFromCache(directory, name).putHeaders(`Last-Modified`(start)))
  }
  
  private def checkResource(path: String): Option[Array[Byte]] = {
    val rs = if (path.endsWith(".js")) {
      val min = path.substring(0, path.length - 3) + ".min.js"
      Option(getClass.getResourceAsStream(sanitize(min))) orElse
      Option(getClass.getResourceAsStream(sanitize(path))) 
    } else {
      Option(getClass.getResourceAsStream(sanitize(path)))
    }
    
    rs map { fin =>
      Process.constant(8 * 1024)
      .toSource
      .through(io.chunkR(fin))
      .runLog
      .run
      .map(_.toArray)
      .toArray
      .flatten
    }
  }
  
  private def getResourceFromCache(directory: String, name: String): Task[Response] = {
    val path = s"$directory/${name.stripPrefix("/")}"
    
    (cache.get(path) orElse checkResource(path)).cata(
      some = bytes => {
        cache.putIfAbsent(path, bytes)
        
        val mime = path.split("\\.").lastOption.flatMap(MediaType.forExtension(_)).getOrElse(MediaType.`application/octet-stream`)        
        Ok(bytes).putHeaders(`Content-Type`(mime))
      } ,
      none = NotFound(s"404 Not Found: '$path'")
    )
  }
}

object ResourceCache {
  def apply() : Cache = new ResourceCache()
  
  private def sanitize(str: String) = 
    """\.\.""".r.replaceAllIn(str, ".")
}