package com.victorursan.producer

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.victorursan.common.serializers.json.JsSerializer
import com.victorursan.data.RandomJsonData
import org.json4s.JsonAST.JValue

import scala.util.Try

object JsSimpleProducer extends SimpleProducer[JValue] {

  override def retrieveSource(): Try[Source[JValue, NotUsed]] = {
    val flowTry = flow(JsSerializer()).map(RandomJsonData().via)
    flowTry.foreach(_ => println(s"Sending JSON data"))
    flowTry
  }
}
