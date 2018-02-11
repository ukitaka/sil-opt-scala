package me.waft.sil.parser

import me.waft.sil.lang._
import me.waft.sil.lang.instruction.AllocStack
import me.waft.swift.lang.`type`.NominalType
import org.scalatest._

class SILBasicBlockParserSpec extends FlatSpec with Matchers with SILBasicBlockParser {
  "SIL basic block parser" should "work well" in {
    val sil =
      """|bb0(%0 : $Dog):
         |  %1 = alloc_stack $Dog
         |  return %1 : $Dog
       """.stripMargin
    val result = basicBlock.parse(sil).get.value
    val dog = NominalType("Dog")
    result.label.identifier should be ("bb0")
    result.label.args.head should be (SILArgument("%0", SILType(dog)))
    result.instructionDefs.head.instruction should be (AllocStack(SILType(dog)))
    result.instructionDefs.head.values.head should be (SILValue("%1"))
    result.terminator should be (Return(SILOperand(SILValue("%1"), SILType(dog))))
  }

  "SIL basic block parser" should "work well without instruction def" in {
    val sil =
      """bb2:
         |  br bb1(undef : $Builtin.Int32, undef : $Builtin.Int32)
       """.stripMargin
    val result = basicBlock.parse(sil).get.value
    result.label.identifier should be ("bb2")
  }
}