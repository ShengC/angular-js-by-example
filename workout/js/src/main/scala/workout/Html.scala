package workout

import scalatags.Text.tags2
import scalatags.Text.all._

object Html {
  def apply() = {
    "<!DOCTYPE html>" + 
    html(
      head(
        meta(charset := "utf-8"),
        meta(httpEquiv := "X-UA-Compatible", content := "IE=edge"),
        tags2.title("7 Minute Workout"),
        meta(name := "description", content := ""),
        meta(name := "viewport", content := "width=device-width, initial-scale=1"),
        link(href := "static/css/bootstrap.min.css"),
        link(href := "static/css/roboto.css"),
        link(href := "static/css/app.css")                
      ),
      body(`ng-app` := "app", `ng-controller` := "WorkoutController")(
        div(cls := "navbar navbar-default navbar-fixed-top top-navbar")(
          div(cls := "container app-controller")(
            div(cls := "navbar-header")(
              h1("7 Minute Workout")    
            )    
          )    
        ),
        div(cls := "container body-content app-container")(
          pre("Current Exercise: {{controller.toJson()}}"),
          pre("Time Left: {{controller.span()}}")
        ),
        p("{{currentExerciseDuration}}")
      )
    )
  }
  
  val `ng-app` = "ng-app".attr
  val `ng-controller` = "ng-controller".attr
}