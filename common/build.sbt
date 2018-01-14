
name := "common"

libraryDependencies ++= {
  Seq(
    "org.json4s" %% "json4s-core" % "3.5.3" withSources() withJavadoc(),
    "org.json4s" %% "json4s-native" % "3.5.3" withSources() withJavadoc(),
    "org.apache.kafka" % "kafka-clients" % "1.0.0" withSources() withJavadoc()
  )
}
