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
    startExercise(detail)
  }
  
  def startExercise(plan: ExerciseDetail) = {
    def go(plan: ExerciseDetail): Promise = {
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
    
    def run(plan: ExerciseDetail): js.Any = {
      go(plan).`then`((_: js.Any) => {
        if (workoutPlan.exercises.nonEmpty) {
          val detail = workoutPlan.exercises.head
          workoutPlan = workoutPlan.copy( exercises = workoutPlan.exercises.tail )
          run( detail )
        } else {
          console.log("Workout complete!")
        }
      }: js.Any)
    }
    
    run(plan)
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
  def percentage() =
    s"""{ 'width': ${(scope.currentExerciseDuration * 100 / duration()) + "%"} }"""    
    
  @JSExport
  def duration() = 
    scope.currentExercise.duration.toSeconds.toInt
    
  @JSExport
  def toJson() = {
    import upickle._
    import upickle.default._
    
    write(scope.currentExercise)
  }
    
  @JSExport
  def title() = 
    scope.currentExercise.detail.title
  
  @JSExport
  def image() = 
    scope.currentExercise.detail.image
    
  private var workoutPlan: WorkoutPlan = _
  private var restExercise: ExerciseDetail = _
  
  init()
}