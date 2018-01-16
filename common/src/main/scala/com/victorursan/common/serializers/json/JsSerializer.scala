package com.victorursan.common.serializers.json

import org.apache.kafka.common.serialization.Serializer
import org.json4s.JsonAST.{JNothing, JValue}
import org.json4s.native.JsonMethods.{compact, render}

class JsSerializer extends Serializer[JValue] {
  override def configure(configs: java.util.Map[String, _], isKey: Boolean): Unit = Unit

  override def serialize(topic: String, data: JValue = JNothing): Array[Byte] = compact(render(data)).getBytes

  override def close(): Unit = Unit
}

object JsSerializer {
  def apply(): JsSerializer = new JsSerializer()
}
