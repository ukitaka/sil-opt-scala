package me.waft.sil.parser

import me.waft.sil.lang.{AllocStack, SILType, SILValue}
import me.waft.swift.lang.`type`.NominalType
import org.scalatest._

class SILInstructionParserSpec extends FlatSpec with Matchers with SILInstructionParser {
  "SIL instruction parser" should "work well with alloc_stack" in {
    val dog = NominalType("Dog")
    val sil = "alloc_stack $Dog"
    val result = silInstruction.parse(sil).get.value
    result should be(AllocStack(SILType(dog)))
  }
  "SIL instruction def parser" should "work well with alloc_stack" in {
    val sil = "%1 = alloc_stack $Dog"
    val dog = NominalType("Dog")
    val result = silInstructionDef.parse(sil).get.value
    result.instruction should be(AllocStack(SILType(dog)))
    result.values.head should be(SILValue("%1"))
  }
}
