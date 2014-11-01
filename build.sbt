name := "scala-connect4"

version := "1.0"

// Swing
libraryDependencies <+= scalaVersion { "org.scala-lang" % "scala-swing" % _ }

// ScalaFX

libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.20-R6"

// testing

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies += "junit" % "junit" % "4.10" % "test"


    