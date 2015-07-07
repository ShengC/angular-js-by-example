lazy val common = Seq(
  scalaVersion := "2.11.6",
  resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases",
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

lazy val guess = project.in(file("guess")).settings( common: _* ).settings(
  name := "guess"
).dependsOn( resource, server )

lazy val resource = project.in(file("resource")).settings( common: _* ).settings(
  name := "resource"  
)

lazy val server = project.in(file("server")).settings( common: _* ).settings(
  name := "server"  
)

lazy val root = (project in file(".")).aggregate( guess, resource, server ).settings( aggregate in update := false )


