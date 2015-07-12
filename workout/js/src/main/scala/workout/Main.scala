package workout

import scala.scalajs.js.annotation._
import org.scalajs.dom

import com.greencatsoft.angularjs._

@JSExport
object Main {  
  dom.document.write(Html())
  
  val app = Angular.module("app", Seq.empty)
  
  app.controller[WorkoutController]  
}