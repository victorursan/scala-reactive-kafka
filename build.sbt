name := "scala-reactive-kafka"

lazy val commonSettings = Seq(
  organization := "com.victorursan",

  scalaVersion := "2.12.4",
  version := "0.1",

  libraryDependencies ++= {
    val Specs2 = "4.0.1"
    val AkkaStreamKafka = "0.18"

    Seq(
      "com.typesafe.akka" %% "akka-stream-kafka" % AkkaStreamKafka withSources() withJavadoc(),

      //tests
      "org.specs2" %% "specs2-core" % Specs2 % Test
    )
  }


)

libraryDependencies ++= Seq(

)

lazy val common = (project in file("common")).settings(commonSettings: _*)
lazy val producer = (project in file("producer")).settings(commonSettings: _*).dependsOn(common)
lazy val consumer = (project in file("consumer")).settings(commonSettings: _*).dependsOn(common)

lazy val root = (project in file(".")).aggregate(common, producer, consumer)
