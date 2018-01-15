package com.victorursan.consumer

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.victorursan.KafkaConfig
import com.victorursan.common.serializers.json.JsDeserializer
import org.apache.kafka.common.serialization.StringDeserializer


object SimpleConsumer extends App with KafkaConfig {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val jsValueDeserializer = new JsDeserializer()
  private val keyDeserializer = new StringDeserializer()

  private val consumerSettings = ConsumerSettings(system, keyDeserializer, jsValueDeserializer)
    .withBootstrapServers(kafkaUrl)
    .withGroupId(kafkaGroupId)

  val subscriptions = Subscriptions.topics(kafkaTopics)
  val source = Consumer.plainSource(consumerSettings, subscriptions)

  source.map(_.value()).runWith(Sink.foreach(println))

}
