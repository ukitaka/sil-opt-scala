package me.waft.sil.parser

import me.waft.sil.lang._
import org.scalatest._

class SILTerminatorParserSpec extends FlatSpec with Matchers with SILTerminatorParser {
  "return" should "be parsed well" in {
    val t = "return %1 : $Dog"
    val result = silTerminator.parse(t).get.value
    result should be(Return(SILOperand(SILValue("%1"), SILType("Dog"))))
  }

  "br" should "be parsed well" in {
    val t =
      """br bb1(%0 : $Builtin.Int32, %1 : $Builtin.Int32)
        |bb1 (%3 : $Builtin.Int32, %4 : $Builtin.Int32):
      """.stripMargin
    val result = silTerminator.parse(t).get.value
    val operands = Seq(
      SILOperand(SILValue("%0"), SILType("Builtin.Int32")),
      SILOperand(SILValue("%1"), SILType("Builtin.Int32"))
    )
    result should be(Br("bb1", operands))
  }
}
