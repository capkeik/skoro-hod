ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "tinkoff-practice-summer-2023",
    scalacOptions += "-Ypartial-unification",
    libraryDependencies ++= Dependencies.allDeps
  )