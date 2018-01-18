package com.victorursan.consumer

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.victorursan.common.helpers.KafkaConfig
import com.victorursan.common.serializers.json.JsDeserializer
import com.victorursan.common.serializers.protobuf.ProtoDeserializer
import org.apache.kafka.common.serialization.{Deserializer, StringDeserializer}


object SimpleConsumer extends App with KafkaConfig {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val deserializer: Deserializer[_] = ProtoDeserializer()
    //JsDeserializer()
  private val keyDeserializer = new StringDeserializer()

  private val consumerSettings = ConsumerSettings(system, keyDeserializer, deserializer)
    .withBootstrapServers(kafkaUrl)
    .withGroupId(kafkaGroupId)

  val subscriptions = Subscriptions.topics(kafkaTopics)
  val source = Consumer.plainSource(consumerSettings, subscriptions)

  source.map(_.value()).runWith(Sink.foreach(println))

}
