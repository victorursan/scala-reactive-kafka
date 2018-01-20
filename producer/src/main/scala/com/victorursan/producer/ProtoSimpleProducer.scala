package com.victorursan.producer

import akka.stream.scaladsl.Sink
import com.victorursan.common.serializers.protobuf.ProtoSerializer
import com.victorursan.common.serializers.protobuf.message.Message
import com.victorursan.data.RandomProtoData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object ProtoSimpleProducer extends SimpleProducer[Message] {

  val sourceTry = getSource(ProtoSerializer[Message]())(RandomProtoData())

  sourceTry.foreach { source =>
    val countSink: Sink[Message, Future[Int]] = Sink.fold[Int, Message](0) { case (count, _) => count + 1 }
    val flow: Future[Int] = source.runWith(countSink)

    flow.onComplete { result: Try[Int] =>
      result.foreach { records => println(s"Sent Proto $records records") }
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
