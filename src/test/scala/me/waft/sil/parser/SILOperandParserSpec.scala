package me.waft.sil.parser

import me.waft.sil.lang.{SILOperand, SILType, SILValue}
import me.waft.swift.lang.`type`.NominalType
import org.scalatest._

class SILOperandParserSpec extends FlatSpec with Matchers with SILOperandParser {
  "SIL operand parser" should "work well" in {
    val sil = "%1 : $*Int"
    val int = NominalType("Int")
    val result = silOperand.parse(sil).get.value
    result should be (SILOperand(SILValue("%1"), SILType(int)))
  }

  "SIL operand parser" should "work well with `undef`" in {
    val sil = "undef : $Builtin.Int32"
    val int = NominalType("Builtin.Int32")
    val result = silOperand.parse(sil).get.value
    result should be (SILOperand(SILValue.undef, SILType(int)))
  }
}
