package com.victorursan.serializers.json

import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods.{compact, parse, render}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.AfterAll

class JsSerializerTest extends Specification with AfterAll with Mockito {

  val serialiazer = new JsSerializer()

  "The JSON Serializer should" >> {
    "be able to serialize an empty JSON" >> {
      val emptyJson = parse(""" {} """)
      serialiazer.serialize(anyString, emptyJson) must_== compact(render(emptyJson)).getBytes
    }

    "be able to serialize any JSON" >> {
      val randomJson: JValue = parse(s"""{"$anyString": $anyInt, "$anyString": ${anyListOf[Int]} }""")
      serialiazer.serialize(anyString, randomJson) must_== compact(render(randomJson)).getBytes
    }
  }

  def afterAll(): Unit = {
    serialiazer.close()
  }

}