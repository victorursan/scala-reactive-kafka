package com.victorursan.helpers

import com.typesafe.config.{Config, ConfigFactory, ConfigValue, ConfigValueFactory}
import com.victorursan.helpers.KafkaHelper.defaultKafkaUrl
import org.specs2.mutable.Specification
import scala.collection.JavaConverters._

class KafkaHelperTest extends Specification {

  "The KafkaHelper" should {
    "return a default kafkaUrl" in {
      KafkaHelper.kafkaUrl() should_== defaultKafkaUrl
    }

    """return a default if the config is present and "kafka.url" is missing""" in {
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
}
