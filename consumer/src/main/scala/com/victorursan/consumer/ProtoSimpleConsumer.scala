package com.victorursan.consumer

import akka.stream.scaladsl.Sink
import com.victorursan.common.serializers.protobuf.ProtoDeserializer
import com.victorursan.common.serializers.protobuf.message.Message

object ProtoSimpleConsumer extends SimpleConsumer[Message] {

  val source = getSource(ProtoDeserializer[Message](Message.messageCompanion))
  source.map(_.value()).runWith(Sink.foreach(println))
}
