package me.waft.sil.optimizer.pass

import me.waft.sil.optimizer.analysis.SILFunctionAnalysis
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
      """|sil @dead2 : $@convention(thin) () -> () {
         |bb0:
         |  br bb1(undef : $Builtin.Int32, undef : $Builtin.Int32)
         |
         |bb1(%1 : $Builtin.Int32, %2 : $Builtin.Int32):
         |  br bb3
         |
         |bb2:
         |  br bb1(undef : $Builtin.Int32, undef : $Builtin.Int32)
         |
         |bb3:
         |  %5 = tuple ()
         |  return %5 : $()
         |}
      """.stripMargin

    val func0 = silFunction.parse(sil).get.value
    val func1 = silFunction.parse(optimizedSil).get.value
    val func2 = DCE.run(func0)
    func1 shouldBe (func2)
  }

  "DCE" should "not eliminate an infinity loop" in {
    val sil =
      """|sil @dead3 : $@convention(thin) () -> () {
         |bb0:
         |  br bb1
         |bb1:
         |  %0 = integer_literal $Builtin.Int32, 0
         |  br bb1
         |}
      """.stripMargin

    // Same as original sil. This means DCE in Swift compiler does not eliminate infinite loop
    // even if optimizer knows it is dead block.
    val func0 = silFunction.parse(sil).get.value
    SILFunctionAnalysis(func0).hasInfiniteLoops should be(true)
    val func2 = DCE.run(func0)
    func0 should be(func2)
  }

  "DCE" should "work well with control-dependent node" in {
    val sil =
    """|sil @control_dependent : $() {
       |bb0:
       |  br bb1
       |bb1:
       |  %1 = integer_literal $Int1, 1
       |  %2 = integer_literal $Int, 2
       |  cond_br %1, bb2, bb4(%2 : $Int)
       |bb2:
       |	br bb3
       |bb3:
       |  %5 = integer_literal $Int, 1
       |	br bb4(%5 : $Int)
       |bb4(%7 : $Int):
       |	return %7 : $Int
       |}
    """.stripMargin

    // FIXME: %1 should be eliminated.
    val optimizedSil =
      """|sil @control_dependent : $() {
         |bb0:
         |  br bb1
         |bb1:
         |  %1 = integer_literal $Int1, 1
         |  %2 = integer_literal $Int, 2
         |  br bb4(%2 : $Int)
         |bb2:
         |	br bb3
         |bb3:
         |  %5 = integer_literal $Int, 1
         |	br bb4(%5 : $Int)
         |bb4(%7 : $Int):
         |	return %7 : $Int
         |}
      """.stripMargin

    val func0 = silFunction.parse(sil).get.value
    val func1 = silFunction.parse(optimizedSil).get.value
    val func2 = DCE.run(func0)
    func1 should be(func2)
  }
}
