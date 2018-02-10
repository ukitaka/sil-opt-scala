package me.waft.sil.parser

import me.waft.sil.lang._
import org.scalatest._

class SILTerminatorParserSpec extends FlatSpec with Matchers with SILTerminatorParser {
  "return" should "be parsed well" in {
    val t = "return %1 : $Dog"
    val result = silTerminator.parse(t).get.value
    result should be(Return(SILOperand(SILValue("%1"), SILType("Dog"))))
  }
}
