package com.victorursan.data

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.victorursan.common.serializers.protobuf.message.Message

import scala.util.Random

object RandomProtoData {

  def apply(): Source[Message, NotUsed] = {
    Source[Message](Stream
      .from(1)
      .take(Random.nextInt(100))
      .map(i => Message(i, randomNumber = i * Random.nextInt(), name = Random.nextString(4))))
  }
}
