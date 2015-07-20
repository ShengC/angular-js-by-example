import scala.concurrent.duration._

package object workout {
  case class Exercise(
    name: String, 
    title: String, 
    description: String, 
    image: String, 
    videos: Seq[String] = Seq.empty,
    variations: Seq[String] = Seq.empty,
    procedure: Option[String] = None)
  
  case class ExerciseDetail(
    detail: Exercise,
    duration: Duration
  )  
    
  case class WorkoutPlan(
    name: String,
    title: String,
    restBetweenExercise: Duration,
    exercises: Seq[ExerciseDetail] = Seq.empty)  
  
  private[workout] val `7-minute-workout` = WorkoutPlan(
      name = "7minWorkout",
      title = "7 Minute Workout",
      restBetweenExercise = FiniteDuration(10, SECONDS),
      exercises = Seq(
        ExerciseDetail(
          detail = Exercise(
            name = "jumpingJacks",
            title = "Jumping Jacks",
            description = "A jumping jack or star jump, also called side-straddle hop is a physical jumping exercise.",
            image = "static/img/JumpingJacks.png",
            videos = Seq(
              "//www.youtube.com/embed/dmYwZH_BNd0", 
              "//www.youtube.com/embed/BABOdJ-2Z6o", 
              "//www.youtube.com/embed/c4DAnQ6DtF8"                  
            ),
            procedure = Some(""""Assume an erect position, with feet together and arms at your side.
                            |Slightly bend your knees, and propel yourself a few inches into the air.
                            |While in air, bring your legs out to the side about shoulder width or slightly wider.
                            |As you are moving your legs outward, you should raise your arms up over your head; arms should be slightly bent throughout the entire in-air movement.
                            |Your feet should land shoulder width or wider as your hands meet above your head with arms slightly bent""".stripMargin)
          ),
          duration = FiniteDuration(30, SECONDS)
        ),
        ExerciseDetail(
          detail = Exercise(
            name = "wallSit",
            title = "Wall Sit",
            description = "A wall sit, also known as a Roman Chair, is an exercise done to strengthen the quadriceps muscles.",
            image = "static/img/wallsit.png",
            videos = Seq(
              "//www.youtube.com/embed/y-wV4Venusw", 
              "//www.youtube.com/embed/MMV3v4ap4ro"                
            ),
            procedure = Some("""Place your back against a wall with your feet shoulder width apart and a little ways out from the wall.
                              |Then, keeping your back against the wall, lower your hips until your knees form right angles.""".stripMargin)
          ),
          duration = FiniteDuration(30, SECONDS)
        ),
        ExerciseDetail(
          detail = Exercise(
            name = "pushUp",
            title = "Push Up",
            description = "A push-up is a common exercise performed in a prone position by raising and lowering the body using the arms",
            image = "static/img/pushup.png",
            videos = Seq(
              "//www.youtube.com/watch?v=Eh00_rniF8E", 
              "//www.youtube.com/watch?v=ZWdBqFLNljc", 
              "//www.youtube.com/watch?v=UwRLWMcOdwI", 
              "//www.youtube.com/watch?v=ynPwl6qyUNM", 
              "//www.youtube.com/watch?v=OicNTT2xzMI"                  
            ),
            variations = Seq(
              "Planche push-ups", 
              "Knuckle push-ups", 
              "Maltese push-ups", 
              "One arm versions"
            ),
            procedure = Some("""Lie prone on the ground with hands placed as wide or slightly wider than shoulder width. 
                              |Keeping the body straight, lower body to the ground by bending arms at the elbows. 
                              |Raise body up off the ground by extending the arms.""".stripMargin)
          ),
          duration = FiniteDuration(30, SECONDS)
        ),
        ExerciseDetail(
          detail = Exercise(
            name = "crunches",
            title = "Abdominal Crushes",
            description = "The basic crunch is a abdominal exercise in a strength-training program.",
            image = "static/img/crunches.png",
            videos = Seq(
              "//www.youtube.com/embed/Xyd_fa5zoEU", 
              "//www.youtube.com/embed/MKmrqcoCZ-M"                
            ),
            procedure = Some("""Lie on your back with your knees bent and feet flat on the floor, hip-width apart.
                              |Place your hands behind your head so your thumbs are behind your ears.
                              |Hold your elbows out to the sides but rounded slightly in.
                              |Gently pull your abdominals inward.
                              |Curl up and forward so that your head, neck, and shoulder blades lift off the floor.
                              |Hold for a moment at the top of the movement and then lower slowly back down.""".stripMargin)
          ),
          duration = FiniteDuration(30, SECONDS)
        ),
        ExerciseDetail(
          detail = Exercise(
            name = "stepUpOntoChair",
            title = "Step Up Onto Chair",
            description = "Step exercises are ideal for building muscle in your lower body.",
            image = "static/img/stepUpOntoChair.jpeg",
            videos = Seq(
              "//www.youtube.com/embed/aajhW7DD1EA"
            ),
            procedure = Some("""Position your chair in front of you.
                              Stand with your feet about hip width apart, arms at your sides.
                              Step up onto the seat with one foot, pressing down while bringing your other foot up next to it. 
                              Step back with the leading foot and bring the trailing foot down to finish one step-up.""".stripMargin)
          ),
          duration = FiniteDuration(30, SECONDS)
        ),
        ExerciseDetail(
          detail = Exercise(
            name = "squat",
            title = "Squat",
            description = "The squat is a compound, full body exercise that trains primarily the muscles of the thighs, hips, buttocks and quads.",
            image = "static/img/squat.png",
            videos = Seq(
              "//www.youtube.com/embed/QKKZ9AGYTi4", 
              "//www.youtube.com/embed/UXJrBgI2RxA"                  
            ),
            procedure = Some( """Stand with your head facing forward and your chest held up and out.
                              |Place your feet shoulder-width apart or little wider. Extend your hands straight out in front of you.
                              |Sit back and down like you're sitting into a chair. Keep your head facing straight as your upper body bends forward a bit. Rather than allowing your back to round, let your lower back arch slightly as you go down.
                              |Lower down so your thighs are parallel to the floor, with your knees over your ankles. Press your weight back into your heels.
                              |Keep your body tight, and push through your heels to bring yourself back to the starting position.""".stripMargin )
          ),
          duration = FiniteDuration(30, SECONDS)
        ),
        ExerciseDetail(
          detail = Exercise(
            name = "tricepdips",
            title = "Tricep Dips on Chair",
            description = "A body weight exercise that targets triceps.",
            image = "static/img/tricedips.jpg",
            videos = Seq(
              "//www.youtube.com/embed/tKjcgfu44sI", 
              "//www.youtube.com/embed/jox1rb5krQI"  
            ),
            procedure = Some( """Sit up on a chair. Your legs should be slightly extended, with your feet flat on the floor.
                              |Place your hands edges of the chair. Your palms should be down, fingertips pointing towards the floor.
                              |Without moving your legs, bring your glutes forward off the chair.
                              |Steadily lower yourself. When your elbows form 90 degrees angles, push yourself back up to starting position""".stripMargin )
          ),
          duration = FiniteDuration(30, SECONDS)
        ),
        ExerciseDetail(
          detail = Exercise(
            name = "plank",
            title = "Plank",
            description = "The plank (also called a front hold, hover, or abdominal bridge) is an isometric core strength exercise that involves maintaining a difficult position for extended periods of time. ",
            image = "static/img/plank.png",
            videos = Seq(
              "//www.youtube.com/embed/pSHjTRCQxIw", 
              "//www.youtube.com/embed/TvxNkmjdhMM"
            ),
            procedure = Some( """Get into pushup position on the floor.
                              |Bend your elbows 90 degrees and rest your weight on your forearms.
                              |Your elbows should be directly beneath your shoulders, and your body should form a straight line from head to feet.
                              |Hold this position.""".stripMargin )
          ),
          duration = FiniteDuration(30, SECONDS)
        ),
        ExerciseDetail(
          detail = Exercise(
            name = "highKnees",
            title = "High Knees",
            description = "A form exercise that develops strength and endurance of the hip flexors and quads and stretches the hip extensors.",
            image = "static/img/highknees.png",
            videos = Seq(
              "//www.youtube.com/embed/OAJ_J3EZkdY", 
              "//www.youtube.com/embed/8opcQdC-V-U"   
            ),
            procedure = Some( """Start standing with feet hip-width apart. 
                              |Do inplace jog with your knees lifting as much as possible towards your chest.""".stripMargin )
          ),
          duration = FiniteDuration(30, SECONDS)
        ),
        ExerciseDetail(
          detail = Exercise(
            name = "lunges",
            title = "Lunges",
            description = "Lunges are a good exercise for strengthening, sculpting and building several muscles/muscle groups, including the quadriceps (or thighs), the gluteus maximus (or buttocks) as well as the hamstrings. ",
            image = "static/img/lunges.png",
            videos = Seq( "//www.youtube.com/embed/Z2n58m2i4jg" ),
            procedure = Some("""Stand erect with your feet about one shoulder width apart.
                              |Put your hands on your hips, keep your back as straight as possible, relax your shoulders and keep your eyes facing directly ahead.
                              |Take a large step forward with one leg.
                              |As you step forward, lower your hips and bend your knees until they both form 90 degree angles.
                              |Return to starting position.
                              |Repeat with your alternate leg.""".stripMargin)
          ),
          duration = FiniteDuration(30, SECONDS)
        ),
        ExerciseDetail(
          detail = Exercise(
            name = "pushupNRotate",
            title = "Pushup And Rotate",
            description = "A variation of pushup that requires you to rotate.",
            image = "static/img/pushupNRotate.jpg",
            videos = Seq( "//www.youtube.com/embed/qHQ_E-f5278" ),
            procedure = Some( """Assume the classic pushup position, but as you come up, rotate your body so your right arm lifts up and extends overhead.
                              |Return to the starting position, lower yourself, then push up and rotate till your left hand points toward the ceiling.""".stripMargin )
          ),
          duration = FiniteDuration(30, SECONDS)
        ),
        ExerciseDetail(
          detail = Exercise(
            name = "sidePlank",
            title = "Side Plank",
            description = "A variation to Plank done using one hand only",
            image = "static/img/sideplank.png",
            videos = Seq( "//www.youtube.com/embed/wqzrb67Dwf8", "//www.youtube.com/embed/_rdfjFSFKMY" ),
            procedure = Some( """Lie on your side, in a straight line from head to feet, resting on your forearm.
                              |Your elbow should be directly under your shoulder.
                              |With your abdominals gently contracted, lift your hips off the floor, maintaining the line.
                              |Keep your hips square and your neck in line with your spine. Hold the position.""".stripMargin )
          ),
          duration = FiniteDuration(30, SECONDS)
        )
      )
    )
}