name := "scala-reactive-kafka"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= {

  val AkkaStreamKafka = "0.18"

  Seq(
    "com.typesafe.akka" %% "akka-stream-kafka" % AkkaStreamKafka withSources() withJavadoc()
  )
}
