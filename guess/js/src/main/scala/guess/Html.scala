package guess

import scalatags.Text.all._
import scalatags.Text.tags2

object Html {
  import AngularTags._
  
  val hd = 
    head(
      tags2.title("Guess The Number!"),
      link( rel := "stylesheet", `type` := "text/css", href := "static/css/bootstrap.min.css" ),
      script( `type` := "text/javascript", src := "static/js/angular.js" ),
      script( `type` := "text/javascript", src := "static/js/guess-dev.js" )
    )
    
  val bd = 
    body( ngApp := "app" )(
      div( cls := "container", ngController := "GuessTheNumberController" )(
        h2("Guess the number"),
        p(cls := "well lead")("Guess the computer generated random number between 1 and 1000."),
        label("Your Guess:"),
        input(`type` := "number", ngModel := "guess"),
        button( ngClick := "controller.verifyGuess()", cls := "btn btn-primary btn-sm" )("Verify"),
        button( ngClick := "controller.initializeGame()", cls := "btn btn-warning btn-sm" )("Restart"),
        p(
          p( ngShow := "deviation<0", cls := "alert alert-warning" )("Your guess is higher."),
          p( ngShow := "deviation>0", cls := "alert alert-warning" )("Your guess is lower."),
          p( ngShow := "deviation==0", cls := "alert alert-success" )("""Yes! That's it""")
        ),
        p( cls := "text-info" )("No of guesses", span( cls := "badge" )("{{noOfTries}}"))
      )
    )
    
    val all = html (hd, bd)
}

object AngularTags {
  val ngApp = "ng-app".attr
  val ngController = "ng-controller".attr
  val ngModel = "ng-model".attr
  val ngClick = "ng-click".attr
  val ngShow = "ng-show".attr
}