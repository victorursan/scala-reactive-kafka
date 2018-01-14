package com.victorursan.common.serializers.json

import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.AfterAll

class JsDeserializerTest extends Specification with AfterAll with Mockito {

  val deserialiazer = new JsDeserializer()

  "The JSON Deserializer" should {
    "be able to deserialize an empty JSON" in {
      val emptyJson = parse(""" {} """)
      deserialiazer.deserialize(anyString, compact(render(emptyJson)).getBytes) must_== emptyJson
    }

    "be able to deserialize any JSON" in {
      val randomJson: JValue = parse(s"""{"$anyString": $anyInt, "$anyString": ${anyListOf[Int]} }""")
      deserialiazer.deserialize(anyString, compact(render(randomJson)).getBytes) must_== randomJson
    }

    "be able to deserialize serialized JSON from JsSerializer" in {
      val serializer = new JsSerializer()
      val randomJson: JValue = parse(s"""{"$anyString": $anyInt, "$anyString": ${anyListOf[Int]} }""")
      val topic = anyString
      val serialized = serializer.serialize(topic, randomJson)
      serializer.close()
      deserialiazer.deserialize(topic, serialized) must_== randomJson
    }
  }

  def afterAll(): Unit = {
    deserialiazer.close()
  }
}
