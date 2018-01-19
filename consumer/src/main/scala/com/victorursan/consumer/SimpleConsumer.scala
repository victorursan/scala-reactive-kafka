package com.victorursan.consumer

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.victorursan.common.helpers.KafkaConfig
import com.victorursan.common.serializers.protobuf.ProtoDeserializer
import com.victorursan.common.serializers.protobuf.message.Message
import org.apache.kafka.common.serialization.{Deserializer, StringDeserializer}


trait SimpleConsumer[T] extends App with KafkaConfig {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def deserializer(): Deserializer[T]

  private val consumerSettings = ConsumerSettings(system, new StringDeserializer(), deserializer())
    .withBootstrapServers(kafkaUrl)
    .withGroupId(kafkaGroupId)
  val subscriptions = Subscriptions.topics(kafkaTopics)
  val source = Consumer.plainSource(consumerSettings, subscriptions)

  source.map(_.value()).runWith(Sink.foreach(println))

}
