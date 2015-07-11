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

lazy val guess = crossProject.in( file("guess") ).settings( common: _* )
.jvmSettings(
  name := "guess"
).jsSettings(
  name := "guess-js",
  libraryDependencies := { libraryDependencies.value.filterNot(_.organization == "org.http4s") },
  libraryDependencies ++= Seq(
    "com.greencatsoft" %%% "scalajs-angular" % "0.5-SNAPSHOT",
    "com.lihaoyi" %%% "scalatags" % "0.5.2"
  )
)

lazy val guessJVM = guess.jvm.dependsOn( resource, server ).dependsOn( guessJS ).settings( Revolver.settings: _* ).settings(
  fork in run := true,
  mainClass in Revolver.reStart := Some( "guess.GuessApp" )
)

lazy val guessJS = guess.js.enablePlugins( ScalaJSPlugin ).settings(
  (artifactPath in fastOptJS) in Compile := { baseDirectory.value / "src/main/resources" / "static/js/guess-dev.js" },
  (artifactPath in fullOptJS) in Compile := { baseDirectory.value / "src/main/resources" / "static/js/guess.js" }
  //packageScalaJSLauncher in Compile := { new Attributed(baseDirectory.value / "src/main/resources" / "static/js/guess-launcher.js")(AttributeMap.empty) },
  //packageJSDependencies in Compile := { baseDirectory.value / "src/main/resources" / "static/js/guess-deps.js" },
  //persistLauncher in Compile := true,
  //persistLauncher in Test := false
)

lazy val resource = project.in(file("resource")).settings( common: _* ).settings(
  name := "resource"
)

lazy val server = project.in(file("server")).settings( common: _* ).settings(
  name := "server"
)

lazy val root = (project in file(".")).aggregate( guessJVM, guessJS, resource, server ).settings( aggregate in update := false )


