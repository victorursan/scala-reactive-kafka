package com.victorursan.common.serializers.protobuf

import java.util

import com.trueaccord.scalapb._
import org.apache.kafka.common.serialization.Deserializer

class ProtoDeserializer[A <: GeneratedMessage with Message[A]](implicit val cmp: GeneratedMessageCompanion[A])
  extends Deserializer[A] {

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = Unit

  override def deserialize(topic: String, data: Array[Byte]): A = cmp.parseFrom(data)

  override def close(): Unit = Unit
}

object ProtoDeserializer {

  def apply[A <: GeneratedMessage with Message[A]](implicit cmp: GeneratedMessageCompanion[A]): ProtoDeserializer[A] = new ProtoDeserializer[A]()
}

