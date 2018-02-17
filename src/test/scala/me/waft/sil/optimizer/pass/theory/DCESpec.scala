package me.waft.sil.optimizer.pass.theory

import me.waft.sil.lang.SILValue
import me.waft.sil.parser.SILFunctionParser
import org.scalatest._

class DCESpec extends FlatSpec with Matchers with SILFunctionParser {
  "@dead1" should "be optimized well" in {
    val sil =
      """sil @dead1 : $@convention(thin) (Int32, Int32) -> Int32 {
        |bb0(%0 : $Int32, %1 : $Int32):
        |  %3 = struct_extract %0 : $Int32, #Int32._value
        |  %4 = struct_extract %1 : $Int32, #Int32._value
        |  %5 = integer_literal $Builtin.Int1, -1
        |  %6 = builtin "sadd_with_overflow_Int32"(%3 : $Builtin.Int32, %4 : $Builtin.Int32, %5 : $Builtin.Int1) : $(Builtin.Int32, Builtin.Int1)
        |  %7 = tuple_extract %6 : $(Builtin.Int32, Builtin.Int1), 0
        |  %8 = struct $Int32 (%7 : $Builtin.Int32)
        |  return %0 : $Int32
        |}
      """.stripMargin

    val optimizedSil =
      """sil @dead1 : $@convention(thin) (Int32, Int32) -> Int32 {
        |bb0(%0 : $Int32, %1 : $Int32):
        |  return %0 : $Int32
        |}
      """.stripMargin

    val func0 = silFunction.parse(sil).get.value
    val func1 = silFunction.parse(optimizedSil).get.value
    val func2 = DCE.run(func0)

    func1 shouldBe (func2)
  }
}
