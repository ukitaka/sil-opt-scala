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

  "SIL function" should "be parsed well No.2" in {
    val sil =
      """sil @simple_promotion : $(Int) -> Int {
        |bb0(%0 : $Int):
        |  %1 = alloc_box $Int
        |  %1a = project_box %1 : $Int
        |  store %0 to %1a : $*Int
        |  %3 = load %1a : $*Int
        |  strong_release %1 : $Int
        |  return %3 : $Int
        |}
      """.stripMargin
    val result = SILFunctionParser.silFunction.parse(sil).get.value
    result.linkage should be (None)
  }
}
