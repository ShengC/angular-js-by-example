package workout

import com.greencatsoft.angularjs._
import com.greencatsoft.angularjs.core._

import scala.concurrent.duration._

import scala.scalajs._
import scala.scalajs.js.annotation._

trait WorkoutScope extends Scope {
  var currentExercise: ExerciseDetail = js.native
  var currentExerciseIndex: Int = js.native
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
    
    scope.currentExerciseIndex = 0
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
          scope.currentExerciseIndex += 1
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
  def nextTitle(): js.UndefOr[String] = {
    import js.JSConverters._
    
    this.workoutPlan.exercises.headOption.map(_.detail.title).orUndefined
  }
  
  var workoutPlan: WorkoutPlan = _
  var restExercise: ExerciseDetail = _
  
  init()
}

trait WorkoutAudioScope extends WorkoutScope {
  var exercisesAudio: js.Array[_ <: js.Object] = js.native
  var nextUpAudio: js.Dynamic = js.native
  var nextUpExerciseAudio: js.Dynamic = js.native
  var halfWayAudio: js.Dynamic = js.native
  var aboutToCompleteAudio: js.Dynamic = js.native
}

@injectable("WorkoutAudioController")
class WorkoutAudioController(scope: WorkoutAudioScope, interval: Interval, timeout: Timeout, location: Location) extends WorkoutController(scope, interval, timeout, location) {
  var workoutPlanwatch: js.Function = scope.`$watch`(() => workoutPlan, (plan: WorkoutPlan) => {
    import js.JSConverters._
    
    scope.exercisesAudio = 
      plan.exercises.map(d => js.Dynamic.literal("src" -> d.detail.nameSound, "type" -> "audio/wav")).toJSArray
      
    workoutPlanwatch.asInstanceOf[js.Function0[js.Any]]()
  })
  
  scope.`$watch`(() => scope.currentExerciseDuration, (d: Int) => {
    if (d == scope.currentExercise.duration.toSeconds / 2 && scope.currentExercise.detail.name != "rest") {
      scope.halfWayAudio.play()
    }
    else if (d == scope.currentExercise.duration.toSeconds - 3) {
      scope.aboutToCompleteAudio.play()
    }
  })
  
  scope.`$watch`(() => scope.currentExercise, (exec: ExerciseDetail) => {
    if (scope.currentExercise.detail.name == "rest") {
      timeout.apply(() => scope.nextUpAudio.play(), 2000)
      timeout.apply(() => scope.nextUpExerciseAudio.play(scope.currentExerciseIndex + 1, true), 3000)
    }
  })
}