package me.waft.parser

import me.waft.parser.instruction.{AllocParser, FunctionApplicationParser, LiteralParser, StructParser}
import me.waft.sil.decl.SILDeclRef
import me.waft.sil.{SILOperand, SILType, SILValue}
import org.scalatest._

class SILInstructionsParserSpec extends FlatSpec with Matchers
  with StructParser with AllocParser with LiteralParser with FunctionApplicationParser {
  "struct_extract instruction" should "be parsed well" in {
    val instruction = "struct_extract %0 : $Bool, #Bool._value"
    val result = structExtract.parse(instruction).get.value
    result.operand should be(SILOperand(SILValue("%0"), SILType("Bool")))
    result.declRef should be(SILDeclRef("Bool._value", None))
  }

  "integer_literal instruction" should "be parsed well" in {
    val instruction = "integer_literal $Builtin.Int1, -1"
    val result = integerLiteral.parse(instruction).get.value
    result.value should be(-1)
    result.`type` should be(SILType("Builtin.Int1"))
  }

  "builtin instruction" should "be parsed well" in {
    val instruction = """builtin "xor_Int1"(%2 : $Builtin.Int1, %3 : $Builtin.Int1) : $Builtin.Int1"""
    val result = builtin.parse(instruction).get.value
    result.name should be("xor_Int1")
    result.`type` should be(SILType("Builtin.Int1"))
  }
}
