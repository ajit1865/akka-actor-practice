ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "akka-actor-practice"
  )
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.8.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.8.2" % Test
)
