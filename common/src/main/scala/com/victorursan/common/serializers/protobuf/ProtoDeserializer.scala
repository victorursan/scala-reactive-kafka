package com.victorursan.common.serializers.protobuf

import java.util

import com.victorursan.common.serializers.protobuf.message.Message
import org.apache.kafka.common.serialization.Deserializer

class ProtoDeserializer extends Deserializer[Message]{
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = Unit

  override def deserialize(topic: String, data: Array[Byte]): Message = Message.parseFrom(data)

  override def close(): Unit = Unit
}

object ProtoDeserializer {
  def apply(): ProtoDeserializer = new ProtoDeserializer();
}

