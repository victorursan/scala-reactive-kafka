package com.victorursan.producer

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.victorursan.common.serializers.protobuf.ProtoSerializer
import com.victorursan.common.serializers.protobuf.message.Message
import com.victorursan.data.RandomProtoData

import scala.util.Try

object ProtoSimpleProducer extends SimpleProducer[Message] {

  override def retrieveSource(): Try[Source[Message, NotUsed]] = {
    val flowTry = flow(ProtoSerializer[Message]()).map(RandomProtoData().via)
    flowTry.foreach(_ => println(s"Sending ProtoData[Message] data"))
    flowTry
  }
}
