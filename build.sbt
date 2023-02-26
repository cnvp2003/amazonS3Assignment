ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "amazonS3Assignment"
  )

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.11.698"
)