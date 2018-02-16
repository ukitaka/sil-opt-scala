package me.waft.sil.parser

import me.waft.sil.lang.SILType
import me.waft.swift.lang.`type`.{NominalType, TupleType}
import org.scalatest._

class SILTypeParserSpec extends FlatSpec with Matchers with SILTypeParser {
  "sil type" should "be parsed well" in {
    val sil = "$Dog Cat"
    silType.parse(sil).get.value shouldBe (SILType("Dog"))
  }

  "sil tuple type" should "be parsed well" in {
    val sil = "$(Builtin.Int32, Builtin.Int1)"
    val swiftType = TupleType(Seq(NominalType("Builtin.Int32"), NominalType("Builtin.Int1")))
    silType.parse(sil).get.value shouldBe (SILType(swiftType))
  }
}
