name := "scala-reactive-kafka"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= {

  val AkaaStreamKafka = "0.18"

  Seq(
    "com.typesafe.akka" %% "akka-stream-kafka" % AkaaStreamKafka withSources() withJavadoc()
  )
}
