package com.victorursan.producer

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.victorursan.common.helpers.KafkaConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{Serializer, StringSerializer}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Try}

trait SimpleProducer[V] extends App with KafkaConfig {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  private val sourceTry: Try[Source[V, NotUsed]] = retrieveSource()

  def retrieveSource(): Try[Source[V, NotUsed]]

  def flow(valueSerializer: Serializer[V]): Try[Flow[V, V, NotUsed]] = {
    val producerSettings = ProducerSettings[String, V](materializer.system, new StringSerializer(), valueSerializer)
      .withBootstrapServers(kafkaUrl)

    val sink = Producer.plainSink(producerSettings).contramap[V](new ProducerRecord(kafkaTopics.head, _))
    Success(Flow[V].alsoTo(sink))
  }

  sourceTry.foreach { source =>
    val countSink: Sink[V, Future[Int]] = Sink.fold[Int, V](0) { case (count, _) => count + 1 }
    val flow = source.runWith(countSink)

    flow.onComplete { result =>
      result.foreach { records => println(s"Sent $records records") }
      system.terminate()
    }
  }

  sourceTry.recover {
    case t: Throwable =>
      println(t.getMessage)
      t.printStackTrace()
      system.terminate()
  }

}
