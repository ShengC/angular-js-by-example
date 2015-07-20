package workout

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core._

import scala.concurrent.duration._

import scala.scalajs._
import scala.scalajs.js.annotation._

trait WorkoutScope extends Scope {
  var currentExercise: ExerciseDetail = js.native
  var currentExerciseDuration: Int = js.native
  var workoutTimeRemaining: Int = js.native
}

@injectable("WorkoutController")
class WorkoutController(scope: WorkoutScope, interval: Interval, timeout: Timeout, location: Location) extends AbstractController[WorkoutScope](scope) {
  
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
    
    scope.workoutTimeRemaining = 
      if (workoutPlan.exercises.isEmpty) 0
      else 
        workoutPlan.exercises.foldLeft(0L)(_ + _.duration.toSeconds).toInt + (workoutPlan.exercises.size - 1) * restExercise.duration.toSeconds.toInt 
    
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
          scope.workoutTimeRemaining -= 1
        },
        1000
      )
    
      timeout.apply(() => { interval.cancel(promise) }, scope.currentExercise.duration.toMillis.toInt + 1000)      
    }
    
    def rest(): Promise = go(restExercise)
    
    def run(plan: ExerciseDetail): js.Any = {
      go(plan).`then`((_: js.Any) => rest().`then`((_: js.Any) => {
        if (workoutPlan.exercises.nonEmpty) {
          val detail = workoutPlan.exercises.head
          workoutPlan = workoutPlan.copy( exercises = workoutPlan.exercises.tail )
          run( detail )
        } else {
          console.log("Workout complete!")
          location.path("/finish")
        }        
      }))
    }
    
    run(plan)
  }
  
//  def totalWorkoutDuration(): Int = {
//    workoutPlan.exercises.foldLeft(0)(_ + _.duration.toSeconds.toInt)
//  }
  
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
    
  @JSExport
  def description() = 
    scope.currentExercise.detail.description
    
  @JSExport
  def procedure(): js.UndefOr[String] = {
    import js.JSConverters._
    
    scope.currentExercise.detail.procedure.orUndefined
  }
   
  @JSExport
  def videos() = {
    import js.JSConverters._
    
    scope.currentExercise.detail.videos.toJSArray
  }
  
  @JSExport
  def name() = {
    scope.currentExercise.detail.name
  }
  
  @JSExport
  def nextTitle() = {
    this.workoutPlan.exercises.head.detail.title
  }
  
  private var workoutPlan: WorkoutPlan = _
  private var restExercise: ExerciseDetail = _
  
  init()
}