package workout

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core._

import scala.concurrent.duration._

import scala.scalajs._
import scala.scalajs.js.annotation._

trait WorkoutScope extends Scope {
  var currentExercise: ExerciseDetail = js.native
  var currentExerciseDuration: Int = js.native
}

@injectable("WorkoutController")
class WorkoutController(scope: WorkoutScope, interval: Interval, timeout: Timeout) extends AbstractController[WorkoutScope](scope) {
  
  import org.scalajs.dom.console
  
  def startWorkout(): Unit = {
    workoutPlan = `7-minute-workout`
    restExercise = ExerciseDetail(
      detail = Exercise(
        name = "rest",
        title = "Relax!",
        description = "Relax a bit",
        image = "static/img/rest.png"
      ),
      duration = workoutPlan.restBetweenExercise
    )
    
    val detail = workoutPlan.exercises.head
    workoutPlan = workoutPlan.copy( exercises = workoutPlan.exercises.tail )
    startExercise(detail).`then`((_: js.Any) => {
      if (workoutPlan.exercises.isEmpty) {
        console.log("Workout complete!")
      } else {
        val detail = workoutPlan.exercises.head
        workoutPlan = workoutPlan.copy( exercises = workoutPlan.exercises.tail )
        startExercise(detail)
      }      
    }: js.Any)
  }
  
  def startExercise(plan: ExerciseDetail) = {
    scope.currentExercise = plan
    scope.currentExerciseDuration = 0
    val promise = interval.apply(
      () => { 
        scope.currentExerciseDuration += 1
      },
      1000
    )
    
    timeout.apply(() => { interval.cancel(promise) }, scope.currentExercise.duration.toMillis.toInt + 1000)
  }
  
  def init(): Unit = {
    startWorkout()
  }
  
//  scope.`$watch`(() => scope.currentExerciseDuration, (n: Int) => {
//    if (n == scope.currentExercise.duration.toSeconds.toInt) {
//      if (workoutPlan.exercises.isEmpty)
//        console.log("Workout complete!")
//      else {
//        val detail = workoutPlan.exercises.head
//        workoutPlan = workoutPlan.copy( exercises = workoutPlan.exercises.tail )
//        startExercise(detail)
//      }
//    }
//  })
  
  @JSExport
  def span() = 
    (scope.currentExercise.duration.toSeconds - scope.currentExerciseDuration).toInt
  
  @JSExport
  def toJson() = {
    import upickle._
    import upickle.default._
    
    write(scope.currentExercise)
  }
    
  private var workoutPlan: WorkoutPlan = _
  private var restExercise: ExerciseDetail = _
  
  init()
}