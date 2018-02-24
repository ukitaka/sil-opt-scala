package me.waft.sil.parser

import me.waft.sil.lang.decl.SILDeclRef
import me.waft.sil.lang.{SILOperand, SILType, SILValue}
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
    val instruction = """witness_method $@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable, #Pingable.ping!1 : <Self where Self : Pingable> (Self) -> () -> (), %2 : $*@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> ()""".stripMargin
    val result = witnessMethod.parse(instruction).get.value
    result.name should be("witness_method")
  }

  "apply instruction" should "be parsed well" in {
    val instruction = """apply %3<@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable>(%2) : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> ()"""
    val result = functionApply.parse(instruction).get.value
    result.name should be("apply")
  }

}
