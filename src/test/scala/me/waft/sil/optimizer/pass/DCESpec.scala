package me.waft.sil.optimizer.pass

import me.waft.sil.lang.SILValue
import me.waft.sil.parser.SILFunctionParser
import org.scalatest._

class DCESpec extends FlatSpec with Matchers with SILFunctionParser {
  "Dead code elimination pass" should "work well" in {
    val sil =
      """sil hidden @_T01d8hogehgoeSiyF : $@convention(thin) () -> Int {
        |bb0:
        |  %0 = integer_literal $Builtin.Int64, 1          // user: %1
        |  %1 = struct $Int (%0 : $Builtin.Int64)          // user: %2
        |  %3 = integer_literal $Builtin.Int64, 2          // user: %4
        |  %4 = struct $Int (%3 : $Builtin.Int64)          // user: %5
        |  return %4 : $Int                                // id: %5
        |}
      """.stripMargin
    val bb = silFunction.parse(sil).get.value.basicBlocks.head
    val optimizedBb = DCE.eliminateDeadCodeInBB(bb)

    // %1 is eliminated
    bb.instructionDefs.exists(_.values.contains(SILValue("%1"))) should be(true)
    optimizedBb.instructionDefs.exists(_.values.contains(SILValue("%1"))) should be(false)

    // and %0 is eliminated
    bb.instructionDefs.exists(_.values.contains(SILValue("%0"))) should be(true)
    optimizedBb.instructionDefs.exists(_.values.contains(SILValue("%0"))) should be(false)
  }

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

    val bb0 = silFunction.parse(sil).get.value.basicBlocks.head
    val bb1 = silFunction.parse(optimizedSil).get.value.basicBlocks.head
    val bb2 = DCE.eliminateDeadCodeInBB(bb0)

    bb1 shouldBe(bb2)
  }
}
