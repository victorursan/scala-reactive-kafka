package com.victorursan.data

import akka.NotUsed
import akka.stream.scaladsl.Source
import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods._

import scala.util.Random

object RandomJsonData {

  def apply(): Source[JValue, NotUsed] = {
    Source[JValue](Stream
      .from(1)
      .take(Random.nextInt(100))
      .map(i => parse( s""" {"${Random.nextString(3)}": ${i * Random.nextDouble()} } """)))
  }

}
