package com.victorursan.common.serializers.json

import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods.{compact, parse, render}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.AfterAll

class JsSerializerTest extends Specification with AfterAll with Mockito {

  val serializer = new JsSerializer()

  "The JSON Serializer" should {
    "be able to serialize an empty JSON" in {
      val emptyJson = parse(""" {} """)
      serializer.serialize(anyString, emptyJson) must_== compact(render(emptyJson)).getBytes
    }

    "be able to serialize any JSON" in {
      val randomJson: JValue = parse(s"""{"$anyString": $anyInt, "$anyString": ${anyListOf[Int]} }""")
      serializer.serialize(anyString, randomJson) must_== compact(render(randomJson)).getBytes
    }
  }

  def afterAll(): Unit = {
    serializer.close()
  }

}
