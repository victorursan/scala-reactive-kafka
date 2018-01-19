package com.victorursan.consumer

import com.victorursan.common.serializers.protobuf.ProtoDeserializer
import com.victorursan.common.serializers.protobuf.message.Message
import org.apache.kafka.common.serialization.Deserializer

object ProtoSimpleConsumer extends SimpleConsumer[Message] {

  override def deserializer(): Deserializer[Message] = ProtoDeserializer[Message](Message.messageCompanion)
}
