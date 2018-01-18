package com.victorursan.common.serializers.protobuf

import java.util

import com.victorursan.common.serializers.protobuf.message.Message
import org.apache.kafka.common.serialization.Serializer

class ProtoSerializer extends Serializer[Message]{
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = Unit

  override def serialize(topic: String, data: Message): Array[Byte] = data.toByteArray

  override def close(): Unit = Unit
}

object ProtoSerializer {
  def apply(): ProtoSerializer = new ProtoSerializer()
}
