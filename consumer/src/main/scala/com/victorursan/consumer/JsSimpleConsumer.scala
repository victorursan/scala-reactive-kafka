package com.victorursan.consumer

import com.victorursan.common.serializers.json.JsDeserializer
import org.apache.kafka.common.serialization.Deserializer
import org.json4s.JsonAST.JValue

object JsSimpleConsumer extends SimpleConsumer[JValue] {

  override def deserializer(): Deserializer[JValue] = JsDeserializer()

}
