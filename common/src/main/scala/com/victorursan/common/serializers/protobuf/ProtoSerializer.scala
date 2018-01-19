package com.victorursan.common.serializers.protobuf

import java.util

import com.trueaccord.scalapb.{GeneratedMessage, Message}
import org.apache.kafka.common.serialization.Serializer

class ProtoSerializer[A <: GeneratedMessage with Message[A]] extends Serializer[A] {

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = Unit

  override def serialize(topic: String, data: A): Array[Byte] = data.toByteArray

  override def close(): Unit = Unit
}

object ProtoSerializer {

  def apply[A <: GeneratedMessage with Message[A]](): ProtoSerializer[A] = new ProtoSerializer[A]()
}
