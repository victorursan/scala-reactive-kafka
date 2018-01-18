package com.victorursan.common.helpers

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._

trait KafkaConfig {
  protected val kafkaConfig: Config = ConfigFactory.parseResourcesAnySyntax("kafka.conf")
    .withFallback(ConfigFactory.parseMap(
      Map(
        "kafka.url" -> "http://192.168.99.100:9092",
        "kafka.topics" -> """["test"]""",
        "kafka.group.id" -> "groupx",
      ).asJava))

  protected lazy val kafkaUrl: String = KafkaHelper.kafkaUrl(kafkaConfig)
  protected lazy  val kafkaTopics: Set[String] = KafkaHelper.kafkaTopics(kafkaConfig)
  protected lazy  val kafkaGroupId: String = KafkaHelper.kafkaGroupId(kafkaConfig)

}
