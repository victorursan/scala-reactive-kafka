package com.victorursan.consumer

import akka.stream.scaladsl.Sink
import com.victorursan.common.serializers.json.JsDeserializer
import org.json4s.JsonAST.JValue

object JsSimpleConsumer extends SimpleConsumer[JValue] {

  val source = getSource(JsDeserializer())
  source.map(_.value()).runWith(Sink.foreach(println))
}
