package me.waft.parser

import me.waft.sil.instruction.AllocStack
import me.waft.sil._
import me.waft.swift.`type`.NominalType
import org.scalatest._
import White._

class CommentParserSpec extends FlatSpec with Matchers {
  "Comment parser" should "work well" in {
    val sil = "// here is comment\n"
    val result = CommentParser.comment.!.parse(sil).get.value
    result should be ("// here is comment\n")
  }

  "Whitespaces parser" should "work well" in {
    val sil = " // here is comment\n // here is also comment"
    val result = whitespaces.!.parse(sil).get.value
    result should be (" // here is comment\n // here is also comment")
  }

  "Parser" should "work well with comment" in {
    val sil =
      """|bb0(%0 : $Dog):
         |  %1 = alloc_stack $Dog // users: %6, %5, %4
         |  return %1 : $Dog
      """.stripMargin
    val result = SILBasicBlockParser.basicBlock.parse(sil).get.value
    val dog = NominalType("Dog")
    result.label.identifier should be ("bb0")
    result.label.args.head should be (SILArgument("%0", SILType(dog)))
    result.instructionDefs.head.instruction should be (AllocStack(SILType(dog)))
    result.instructionDefs.head.values.head should be (SILValue("%1"))
    result.terminator should be (Return(SILOperand(SILValue("%1"), SILType(dog))))
  }
}
