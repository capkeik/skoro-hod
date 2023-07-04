ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "tinkoff-practice-summer-2023",
    libraryDependencies ++= Dependencies.allDeps,
    dependencyOverrides += "io.circe" %% "circe-core" % Dependencies.V.circe
  )
