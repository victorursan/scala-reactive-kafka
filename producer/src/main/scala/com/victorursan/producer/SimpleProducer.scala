package com.victorursan.producer

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.victorursan.common.helpers.KafkaConfig
import com.victorursan.common.serializers.json.JsSerializer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.json4s.JsonAST.JValue

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Try}

object SimpleProducer extends App with KafkaConfig {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  private val jsSerializer = JsSerializer()
  private val stringSerializer = new StringSerializer()

  val sourceTry: Try[Source[JValue, NotUsed]] = {
    val source = RandomJsonData()
    val flowTry = jsFlow().map(source.via)
    flowTry.foreach(_ => println(s"Sending data"))
    flowTry
  }
  def jsFlow(): Try[Flow[JValue, JValue, NotUsed]] = {
    val producerSettings = ProducerSettings[String, JValue](materializer.system, stringSerializer, jsSerializer)
      .withBootstrapServers(kafkaUrl)

    val sink = Producer.plainSink(producerSettings).contramap[JValue](new ProducerRecord(kafkaTopics.head, _))
    Success(Flow[JValue].alsoTo(sink))
  }

  sourceTry.foreach { source =>
    val countSink = Sink.fold[Int, JValue](0) { case (count, _) => count + 1 }

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
