package guess

import resource._
import server._

object GuessApp extends App {
  val route = new TestRoute() orElse new CacheRoute()
  
  val server = CustomServer.newBuilder.bindLocal().addService(route.svc).result()  
  server.awaitShutdown()
}