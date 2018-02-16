package me.waft.sil.parser

import me.waft.sil.lang.{Return, SILOperand, SILType, SILValue}
import me.waft.swift.lang.`type`.NominalType
import org.scalatest._

class CommonParserSpec extends FlatSpec with Matchers with SILBasicBlockParser {

  import WhiteSpaceApi._

  "String literal" should "be parsed well" in {
    val s = "\"hello, world!\""
    stringLiteral.parse(s).get.value should be("hello, world!")
  }

  "Whitespaces parser" should "work well" in {
    val sil = " // here is comment\n // here is also comment"
    val result = whitespaces.!.parse(sil).get.value
    result should be(" // here is comment\n // here is also comment")
  }

  "Comment parser" should "work well" in {
    val sil = "// here is comment\n"
    val result = comment.!.parse(sil).get.value
    result should be("// here is comment\n")
  }

  "Parser" should "work well with comment" in {
    val sil =
      """|bb0(%0 : $Dog):
         |  %1 = alloc_stack $Dog // users: %6, %5, %4
         |  return %1 : $Dog
      """.stripMargin
    val result = basicBlock.parse(sil).get.value
    val dog = NominalType("Dog")
    result.label.identifier should be("bb0")
    result.terminator should be(Return(SILOperand(SILValue("%1"), SILType(dog))))
  }
}
