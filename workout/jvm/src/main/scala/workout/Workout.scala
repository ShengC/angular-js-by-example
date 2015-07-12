package workout

import server._
import resource._

object WorkoutApp extends App {
  CustomServer.newBuilder.bindLocal().addService(new CacheRoute().svc).result().awaitShutdown()
}