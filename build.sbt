ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "untitled"
  )
libraryDependencies += "org.slf4j" % "slf4j-api" % "2.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "5.2.0" % Test