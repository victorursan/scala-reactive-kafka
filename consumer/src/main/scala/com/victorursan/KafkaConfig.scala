package com.victorursan

import com.typesafe.config.{Config, ConfigFactory}
import com.victorursan.common.helpers.KafkaHelper

import scala.collection.JavaConverters._

trait KafkaConfig {
  protected val kafkaConfig: Config = ConfigFactory.parseResourcesAnySyntax("kafka.conf")
    .withFallback(ConfigFactory.parseMap(
      Map(
        "kafka.url" -> "http://localhost:9092",
        "kafka.topics" -> """["test"]""",
        "kafka.group.id" -> "groupx",
      ).asJava))

  protected val kafkaUrl: String = KafkaHelper.kafkaUrl(kafkaConfig)
  protected val kafkaTopics: Set[String] = KafkaHelper.kafkaTopics(kafkaConfig)
  protected val kafkaGroupId: String = KafkaHelper.kafkaGroupId(kafkaConfig)

}
