package com.victorursan.common.serializers.json

import java.io.ByteArrayInputStream

import org.apache.kafka.common.serialization.Deserializer
import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods.parse

class JsDeserializer extends Deserializer[JValue] {
  override def configure(configs: java.util.Map[String, _], isKey: Boolean): Unit = Unit

  override def deserialize(topic: String, data: Array[Byte]): JValue = parse(new ByteArrayInputStream(data))

  override def close(): Unit = Unit
}
