package com.victorursan.common.helpers

import java.net.URI

import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try

object KafkaHelper {

  val defaultKafkaUrl = "localhost:9092"

  def kafkaUrl(config: Config = ConfigFactory.empty()): String = maybeKafkaUrl(config).getOrElse(defaultKafkaUrl)

  private[this] def maybeKafkaUrl(config: Config): Try[String] =
    Try(config.getString("kafka.url")).map { kafkaUrl =>
      val kafkaUrls = kafkaUrl.split(",").map { urlString =>
        val uri = new URI(urlString)
        Seq(uri.getHost, uri.getPort).mkString(":")
      }
      kafkaUrls.mkString(",")
    }


}
