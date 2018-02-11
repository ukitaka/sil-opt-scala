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

    val func0 = silFunction.parse(sil).get.value
    val func1 = silFunction.parse(optimizedSil).get.value
    val func2 = DCE.eliminateDeadCode(func0)

    func1 shouldBe(func2)
  }

  "@dead2" should "be optimized well" in {
    val sil =
      """sil @dead2 : $@convention(thin) () -> () {
        |bb0:
        |  %0 = integer_literal $Builtin.Int32, 2
        |  %1 = integer_literal $Builtin.Int32, 0
        |  br bb1(%0 : $Builtin.Int32, %1 : $Builtin.Int32)
        |bb1(%3 : $Builtin.Int32, %4 : $Builtin.Int32):
        |  %5 = integer_literal $Builtin.Int32, 100
        |  %7 = builtin "cmp_slt_Int32"(%4 : $Builtin.Int32, %5 : $Builtin.Int32) : $Builtin.Int1
        |  cond_br %7, bb2, bb3
        |bb2:
        |  %9 = integer_literal $Builtin.Int32, 3
        |  %11 = integer_literal $Builtin.Int1, -1
        |  %12 = builtin "sadd_with_overflow_Int32"(%3 : $Builtin.Int32, %9 : $Builtin.Int32, %11 : $Builtin.Int1) : $(Builtin.Int32, Builtin.Int1)
        |  %13 = tuple_extract %12 : $(Builtin.Int32, Builtin.Int1), 0
        |  %14 = integer_literal $Builtin.Int32, 1
        |  %15 = builtin "sadd_with_overflow_Int32"(%4 : $Builtin.Int32, %14 : $Builtin.Int32, %11 : $Builtin.Int1) : $(Builtin.Int32, Builtin.Int1)
        |  %16 = tuple_extract %15 : $(Builtin.Int32, Builtin.Int1), 0
        |  br bb1(%13 : $Builtin.Int32, %16 : $Builtin.Int32)
        |bb3:
        |  %18 = tuple ()
        |  return %18 : $()
        |}
      """.stripMargin

    val optimizedSil =
      """sil @dead2 : $@convention(thin) () -> () {
        |bb0:
        |  br bb1(undef : $Builtin.Int32, undef : $Builtin.Int32)
        |bb1(%1 : $Builtin.Int32, %2 : $Builtin.Int32):
        |  br bb3
        |bb2:
        |  br bb1(undef : $Builtin.Int32, undef : $Builtin.Int32)
        |bb3:
        |  %5 = tuple ()
        |  return %5 : $()
        |}
      """.stripMargin

    val func0 = silFunction.parse(sil).get.value.basicBlocks.head
    val func1 = silFunction.parse(optimizedSil).get.value.basicBlocks.head
    val func2 = DCE.eliminateDeadCodeInBB(func0)

    func1 shouldBe(func2)
  }
}
