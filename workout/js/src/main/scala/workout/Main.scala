package workout

import scala.scalajs.js.annotation._
import org.scalajs.dom

import com.greencatsoft.angularjs._

@JSExport
object Main {
  val html = Html()
  
  dom.document.write(html)
  
  val app = Angular.module("app", Seq("ngRoute", "ngSanitize", "mediaPlayer"))
  
  app.config[WorkoutConfig]
  
  app.controller[WorkoutController]  
  
  app.controller[WorkoutAudioController]
  
  app.filter[WorkoutFilter]
  
  app.filter[LineBreakFilter]
}