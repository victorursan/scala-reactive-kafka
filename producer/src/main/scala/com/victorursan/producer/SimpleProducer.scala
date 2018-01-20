package com.victorursan.producer

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Source}
import com.victorursan.common.helpers.KafkaConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{Serializer, StringSerializer}

import scala.util.{Success, Try}

trait SimpleProducer[V] extends App with KafkaConfig {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def getSource(serializer: Serializer[V])(dataGenerator: Source[V, NotUsed]): Try[Source[V, NotUsed]] = {
    val flowTry = flow(serializer).map(dataGenerator.via)
    flowTry.foreach(_ => println(s"Sending data"))
    flowTry
  }

  def flow(valueSerializer: Serializer[V]): Try[Flow[V, V, NotUsed]] = {
    val producerSettings = ProducerSettings[String, V](materializer.system, new StringSerializer(), valueSerializer)
      .withBootstrapServers(kafkaUrl)

    val sink = Producer.plainSink(producerSettings).contramap[V](new ProducerRecord(kafkaTopics.head, _))
    Success(Flow[V].alsoTo(sink))
  }

}
