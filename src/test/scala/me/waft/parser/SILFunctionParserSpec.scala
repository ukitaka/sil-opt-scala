package me.waft.parser

import me.waft.sil.Hidden
import org.scalatest._

class SILFunctionParserSpec extends FlatSpec with Matchers {
  "SIL function" should "be parsed well" in {
    val sil =
      """sil hidden @_T03notAAS2b4bool_tF : $@convention(thin) (Bool) -> Bool {
       |bb0(%0 : $Bool):
       |  %2 = struct_extract %0 : $Bool, #Bool._value
       |  %3 = integer_literal $Builtin.Int1, -1
       |  %4 = builtin "xor_Int1"(%2 : $Builtin.Int1, %3 : $Builtin.Int1) : $Builtin.Int1
       |  %5 = struct $Bool (%4 : $Builtin.Int1)
       |  return %5 : $Bool
       |}
     """.stripMargin
    val result = SILFunctionParser.silFunction.parse(sil).get.value
    result.linkage.get should be (Hidden)
  }
}
