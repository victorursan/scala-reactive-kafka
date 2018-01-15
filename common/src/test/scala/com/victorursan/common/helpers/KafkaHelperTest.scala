package com.victorursan.common.helpers

import com.typesafe.config.{Config, ConfigFactory}
import com.victorursan.common.helpers.KafkaHelper._
import org.specs2.mutable.Specification

import scala.collection.JavaConverters._

class KafkaHelperTest extends Specification {

  "The KafkaHelper kafkaUrl" should {
    "return a default kafkaUrl" in {
      KafkaHelper.kafkaUrl() should_== defaultKafkaUrl
    }

    """return a defaultUrl if the config is present and "kafka.url" is missing""" in {
      val simpleConfig: Config = ConfigFactory.empty()
      KafkaHelper.kafkaUrl(simpleConfig) should_== defaultKafkaUrl
    }

    """return the value in config, present for "kafka.url" """ in {
      val simpleConfig: Config = ConfigFactory.parseMap(Map("kafka.url" -> "http://192.168.10.10:8080").asJava)
      KafkaHelper.kafkaUrl(simpleConfig) should_== "192.168.10.10:8080"
    }

    """return the values in config, present for "kafka.url" """ in {
      val simpleConfig: Config = ConfigFactory.parseMap(Map("kafka.url" ->
        "http://192.168.10.10:8080,http://192.168.10.11:8180,http://192.168.10.12:8080").asJava)
      KafkaHelper.kafkaUrl(simpleConfig) should_== "192.168.10.10:8080,192.168.10.11:8180,192.168.10.12:8080"
    }
  }

  "The KafkaHelper kafkaTopics" should {
    "return a default kafkaTopics" in {
      KafkaHelper.kafkaTopics() should_== defaultTopics
    }

    """return a defaultTopic if the config is present and "kafka.topics" is missing""" in {
      val simpleConfig: Config = ConfigFactory.empty()
      KafkaHelper.kafkaTopics(simpleConfig) should_== defaultTopics
    }

    """return the value in config, present for "kafka.topics" """ in {
      val simpleConfig: Config = ConfigFactory.parseMap(Map("kafka.topics" -> List("a", "b").asJava).asJava)
      KafkaHelper.kafkaTopics(simpleConfig) should_== Set("a", "b")
    }
  }

  "The KafkaHelper kafkaGroupId" should {
    "return a default kafkaUrl" in {
      KafkaHelper.kafkaGroupId() should_== defaultGroupId
    }

    """return a default if the config is present and "kafka.group.id" is missing""" in {
      val simpleConfig: Config = ConfigFactory.empty()
      KafkaHelper.kafkaGroupId(simpleConfig) should_== defaultGroupId
    }

    """return the value in config, present for "kafka.group.id" """ in {
      val simpleConfig: Config = ConfigFactory.parseMap(Map("kafka.group.id" -> "testGroup").asJava)
      KafkaHelper.kafkaGroupId(simpleConfig) should_== "testGroup"
    }
  }

}
