package com.victorursan.producer

import akka.stream.scaladsl.Sink
import com.victorursan.common.serializers.json.JsSerializer
import com.victorursan.data.RandomJsonData
import org.json4s.JsonAST.JValue

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object JsSimpleProducer extends SimpleProducer[JValue] {

  val sourceTry = getSource(JsSerializer())(RandomJsonData())

  sourceTry.foreach { source =>
    val countSink: Sink[JValue, Future[Int]] = Sink.fold[Int, JValue](0) { case (count, _) => count + 1 }
    val flow: Future[Int] = source.runWith(countSink)

    flow.onComplete { result: Try[Int] =>
      result.foreach { records => println(s"Sent JSON $records records") }
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
