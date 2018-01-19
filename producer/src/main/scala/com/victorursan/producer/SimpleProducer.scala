package com.victorursan.producer

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.victorursan.common.helpers.KafkaConfig
import com.victorursan.common.serializers.json.JsSerializer
import com.victorursan.common.serializers.protobuf.ProtoSerializer
import com.victorursan.common.serializers.protobuf.message.Message
import com.victorursan.data.RandomProtoData
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.json4s.JsonAST.JValue

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Try}

trait SimpleProducer extends App  with KafkaConfig {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  private val protoSerializer = ProtoSerializer[Message]()
  private val stringSerializer = new StringSerializer()

  def retrieveSource(source: => Source[Message, NotUsed]): Try[Source[Message, NotUsed]] = {
    val flowTry = protoFlow().map(source.via)
    flowTry.foreach(_ => println(s"Sending Proto data"))
    flowTry
  }

  val protoSourceTry: Try[Source[Message, NotUsed]] = retrieveSource(RandomProtoData())

//  private val jsSerializer = JsSerializer()

//  def jsFlow(): Try[Flow[JValue, JValue, NotUsed]] = {
//    val producerSettings = ProducerSettings[String, JValue](materializer.system, stringSerializer, jsSerializer)
//      .withBootstrapServers(kafkaUrl)
//
//    val sink = Producer.plainSink(producerSettings).contramap[JValue](new ProducerRecord(kafkaTopics.head, _))
//    Success(Flow[JValue].alsoTo(sink))
//  }

//  def flow()

  //  val jsonSourceTry: Try[Source[JValue, NotUsed]] = {
  //    val source = RandomJsonData()
  //    val flowTry = jsFlow().map(source.via)
  //
  //    flowTry.foreach(_ => println(s"Sending JSON data"))
  //    flowTry
  //  }
  //
  //  jsonSourceTry.foreach { source =>
  //    val countSink = Sink.fold[Int, JValue](0) { case (count, _) => count + 1 }
  //    val flow = source.runWith(countSink)
  //
  //    flow.onComplete { result =>
  //      result.foreach { records => println(s"Sent JSON $records records") }
  //      system.terminate()
  //    }
  //  }
  //
  //  jsonSourceTry.recover {
  //    case t: Throwable =>
  //      println(t.getMessage)
  //      t.printStackTrace()
  //      system.terminate()
  //  }

  def protoFlow(): Try[Flow[Message, Message, NotUsed]] = {
    val producerSettings = ProducerSettings[String, Message](materializer.system, stringSerializer, protoSerializer)
      .withBootstrapServers(kafkaUrl)

    val sink = Producer.plainSink(producerSettings).contramap[Message](new ProducerRecord(kafkaTopics.head, _))
    Success(Flow[Message].alsoTo(sink))
  }

  protoSourceTry.foreach { source =>
    val countSink = Sink.fold[Int, Message](0) { case (count, _) => count + 1 }
    val flow = source.runWith(countSink)

    flow.onComplete { result =>
      result.foreach { records => println(s"Sent Proto $records records") }
      system.terminate()
    }
  }

  protoSourceTry.recover {
    case t: Throwable =>
      println(t.getMessage)
      t.printStackTrace()
      system.terminate()
  }

}
