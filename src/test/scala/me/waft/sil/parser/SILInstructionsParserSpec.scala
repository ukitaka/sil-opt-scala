package me.waft.sil.parser

import me.waft.sil.lang.decl.SILDeclRef
import me.waft.sil.lang.{SILOperand, SILType, SILValue}
import me.waft.swift.lang.`type`.GenericFunctionType
import org.scalatest._

class SILInstructionsParserSpec extends FlatSpec with Matchers with SILInstructionParser {

  "struct_extract instruction" should "be parsed well" in {
    val instruction = "struct_extract %0 : $Bool, #Bool._value"
    val result = structExtract.parse(instruction).get.value
    result.operand should be(SILOperand(SILValue("%0"), SILType("Bool")))
    result.declRef should be(SILDeclRef("Bool._value", None, None))
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
    result.functionName should be("xor_Int1")
    result.`type` should be(SILType("Builtin.Int1"))
  }

  "struct instruction" should "be parsed well" in {
    val instruction = "struct $Bool (%4 : $Builtin.Int1)"
    val result = struct.parse(instruction).get.value
    result.operands.head should be(SILOperand(SILValue("%4"), SILType("Builtin.Int1")))
    result.`type` should be(SILType("Bool"))
  }

  "empty tuple instruction" should "be parsed well" in {
    val instruction = "tuple ()"
    val result = tuple.parse(instruction).get.value
    result.operands should be(Seq.empty)
  }

  "witness_method instruction" should "be parsed well" in {
    val instruction = """witness_method $@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable, #Pingable.ping!1, %2 : $*@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> ()"""
    val result = witnessMethod.parse(instruction).get.value
    result.name should be("witness_method")
  }

  "witness_method instruction" should "be parsed well 2" in {
    val instruction = """witness_method $@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable, #Pingable.ping!1 : <Self where Self : Pingable> (Self) -> () -> (), %2 : $*@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> ()"""
    val result = witnessMethod.parse(instruction).get.value
    result.name should be("witness_method")
  }

  "apply instruction" should "be parsed well" in {
    val instruction = """apply %3<@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable>(%2) : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> ()"""
    val result = functionApply.parse(instruction).get.value
    result.name should be("apply")
  }

  "open_existential_ref instruction" should "be parsed well" in {
    val instruction = """open_existential_ref %0 : $Proto to $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto""""
    val result = openExistentialRef.parse(instruction).get.value
    result.name should be("open_existential_ref")
  }

  "class_method instruction" should "be parsed well" in {
    val instruction = """class_method %10 : $Ping, #Ping.ping!1 : (Ping) -> () -> Ping, $@convention(method) (@guaranteed Ping) -> @owned Ping"""
    val result = classMethod.parse(instruction).get.value
    result.name should be("class_method")
  }

}
