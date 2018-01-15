package com.victorursan.common.helpers

import java.net.URI

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._
import scala.util.Try

object KafkaHelper {

  val defaultKafkaUrl = "localhost:9092"
  val defaultTopics = Set("test")
  val defaultGroupId = "groupId"

  def kafkaUrl(config: Config = ConfigFactory.empty()): String = maybeKafkaUrl(config).getOrElse(defaultKafkaUrl)

  private[this] def maybeKafkaUrl(config: Config): Try[String] =
    Try(config.getString("kafka.url"))
      .map(_.split(",")
        .map { urlString =>
          val uri = new URI(urlString)
          Seq(uri.getHost, uri.getPort).mkString(":")
        }.mkString(",")
      )

  def kafkaTopics(config: Config = ConfigFactory.empty()): Set[String] = maybeKafkaTopics(config) match {
    case x if x.nonEmpty => x
    case _ => defaultTopics
  }

  private[this] def maybeKafkaTopics(config: Config): Set[String] =
    Try(config.getList("kafka.topics")).map(_.unwrapped().asScala.flatMap {
      case str: String => Some(str)
      case _ => None
    }.toSet).getOrElse(Set())

  def kafkaGroupId(config: Config = ConfigFactory.empty()): String = maybeKafkaGroupId(config).getOrElse(defaultGroupId)

  private[this] def maybeKafkaGroupId(config: Config): Try[String] = Try(config.getString("kafka.group.id"))
}
