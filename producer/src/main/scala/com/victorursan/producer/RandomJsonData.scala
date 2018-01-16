package com.victorursan.producer

import akka.NotUsed
import akka.stream.scaladsl.Source
import org.json4s.JsonAST.JValue

import scala.util.Random
import org.json4s.native.JsonMethods._

object RandomJsonData {

  def apply(): Source[JValue, NotUsed] = {
    Source[JValue](Stream
      .from(1)
      .take(Random.nextInt(100))
      .map(i =>  parse( s""" {"${Random.nextString(3)}": ${i * Random.nextDouble()} } """ )))
  }

}
