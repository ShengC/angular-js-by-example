import spray.revolver.RevolverPlugin._

lazy val common = Seq(
  scalaVersion := "2.11.6",
  resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases",
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  libraryDependencies ++= Seq(
    "org.http4s" %% "http4s-dsl" % "0.8.2",
    "org.http4s" %% "http4s-blazeserver" % "0.8.2",
    "org.http4s" %% "http4s-json4s-jackson" % "0.8.2"
  ),
  unmanagedSourceDirectories in Compile := Seq( (scalaSource in Compile).value ),
  unmanagedSourceDirectories in Compile += ( baseDirectory.value / "src/main/resources" ),
  unmanagedSourceDirectories in Test := Seq(),
  EclipseKeys.withSource := true
)

def jsCommon(nm: String) = Seq(
  libraryDependencies := { libraryDependencies.value.filterNot(_.organization == "org.http4s") },
  libraryDependencies ++= Seq(
    "com.greencatsoft" %%% "scalajs-angular" % "0.5-SNAPSHOT",
    "com.lihaoyi" %%% "scalatags" % "0.5.2"
  ),
  (artifactPath in fastOptJS) in Compile := { baseDirectory.value / "src/main/resources" / s"static/js/$nm-dev.js" },
  (artifactPath in fullOptJS) in Compile := { baseDirectory.value / "src/main/resources" / s"static/js/$nm.js" }
)

lazy val guess = crossProject.in( file("guess") ).settings( common: _* )
.jvmSettings( name := "guess" )
.jsSettings( name := "guess-js" )

lazy val guessJVM = guess.jvm.dependsOn( resource, server ).dependsOn( guessJS ).settings( Revolver.settings: _* ).settings(
  fork in run := true,
  mainClass in Revolver.reStart := Some( "guess.GuessApp" )
)

lazy val guessJS = guess.js.enablePlugins( ScalaJSPlugin ).settings( jsCommon("guess"): _* )

lazy val workout = crossProject.in( file("workout") ).settings( common: _* )
.jvmSettings( name := "workout" )
.jsSettings( name := "workout-js" )

lazy val workoutJVM = workout.jvm.dependsOn( resource, server ).dependsOn( workoutJS ).settings( Revolver.settings: _* ).settings(
  fork in run := true,
  mainClass in Revolver.reStart := Some( "workout.WorkoutApp" )
)

lazy val workoutJS = workout.js.enablePlugins( ScalaJSPlugin ).settings( jsCommon("workout"): _* ).settings(
  libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.3.2"
)

lazy val resource = project.in(file("resource")).settings( common: _* ).settings(
  name := "resource"
)

lazy val server = project.in(file("server")).settings( common: _* ).settings(
  name := "server"
)

lazy val root = (project in file(".")).aggregate( workoutJVM, workoutJS, guessJVM, guessJS, resource, server ).settings( aggregate in update := false )


