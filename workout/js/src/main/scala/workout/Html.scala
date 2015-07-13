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
        link( rel := "stylesheet", `type` := "text/css", href := "static/css/bootstrap.min.css" ),
        link( rel := "stylesheet", `type` := "text/css", href := "static/css/roboto.css" ),
        link( rel := "stylesheet", `type` := "text/css", href := "static/css/app.css" )                
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
          div(cls := "row")(
            div(id := "exercise-pane", cls := "col-sm-8 col-sm-offset-2")(
              div(cls := "row workout-content")(
                div(cls := "workout-display-div")(
                  h1("{{controller.title()}}"),
                  img(cls := "img-responsive", `ng-src` := "{{controller.image()}}" ),
                  div(cls := "progress time-progress")(
                    div(
                      cls := "progress-bar", 
                      role := "progressbar", 
                      aria.valuenow := "0", 
                      aria.valuemin := "0",
                      aria.valuemax := "{{controller.duration()}}",
                      `ng-style` := "{ 'width': (currentExerciseDuration / controller.duration()) * 100 + '%' }"
                    )    
                  )                  
                ),
                h1("Time Remaining: {{controller.span()}}")
              )    
            )
          )              
        )
      )
    )
  }
  
  val `ng-app` = "ng-app".attr
  val `ng-controller` = "ng-controller".attr
  val `ng-src` = "ng-src".attr
  val `ng-style` = "ng-style".attr
}