import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "me.waft",
      scalaVersion := "2.12.3",
      version      := "1.0.0-SNAPSHOT"
    )),
    name := "sil-scala",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "fastparse" % "1.0.0",
      "org.scalactic" %% "scalactic" % "3.0.4",
      "org.scalatest" %% "scalatest" % "3.0.4" % "test"
    )
  )
