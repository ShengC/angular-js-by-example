package guess

import scalajs._
import scalajs.js.annotation._
import org.scalajs.dom

import com.greencatsoft.angularjs._

@JSExport
object Main {  
  dom.document.write(Html.all.render)
  
  val app = Angular.module("app", Seq.empty)
  app.controller[Controller]
}