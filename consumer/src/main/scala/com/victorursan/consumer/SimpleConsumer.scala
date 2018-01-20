package com.victorursan.consumer

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import com.victorursan.common.helpers.KafkaConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.{Deserializer, StringDeserializer}


trait SimpleConsumer[T] extends App with KafkaConfig {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def getSource(deserializer: Deserializer[T]): Source[ConsumerRecord[String, T], Consumer.Control] = {
    val consumerSettings = ConsumerSettings(system, new StringDeserializer(), deserializer)
      .withBootstrapServers(kafkaUrl)
      .withGroupId(kafkaGroupId)

    val subscriptions = Subscriptions.topics(kafkaTopics)
    Consumer.plainSource(consumerSettings, subscriptions)
  }


}
