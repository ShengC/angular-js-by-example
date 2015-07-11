package guess

import scalajs._
import scalajs.js._

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core._

trait GuessScope extends Scope {
  var guess: Int = js.native
  var deviation: Int = js.native
  var noOfTries: Int = js.native
  var original: Int = js.native
}

@injectable("GuessTheNumberController")
class Controller(scope: GuessScope) extends AbstractController[GuessScope](scope) {
    
  @annotation.JSExport
  def verifyGuess(): Unit = {
    scope.deviation = scope.original - scope.guess
    scope.noOfTries += 1
  }
  
  @annotation.JSExport
  def initializeGame(): Unit = {
    scope.noOfTries = 0
    scope.guess = -1    
    scope.original = random.nextInt(1000) 
  }
  
  private val random = new java.util.Random(new java.util.Date().getTime)
  
  initializeGame()
}