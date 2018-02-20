package me.waft.sil.optimizer.pass

import me.waft.sil.parser.SILFunctionParser
import org.scalatest.{FlatSpec, Matchers}

class CSESpec extends FlatSpec with Matchers with SILFunctionParser {
  val optimizer = ignore // it

  optimizer should "optimize @test0 well" in {
    val sil =
      """|sil @test0 : $@convention(thin) () -> (Builtin.Int8, Builtin.Int8, Builtin.Int16,
         |                           Builtin.Int8) {
         |    %0 = integer_literal $Builtin.Int8, 8
         |    %1 = integer_literal $Builtin.Int8, 8
         |    %2 = integer_literal $Builtin.Int16, 8
         |    %3 = integer_literal $Builtin.Int8, 1
         |    %4 = tuple(%0 : $Builtin.Int8, %1 : $Builtin.Int8, %2 : $Builtin.Int16, %3 : $Builtin.Int8)
         |    return %4 :  $(Builtin.Int8, Builtin.Int8, Builtin.Int16, Builtin.Int8)
         |}
      """.stripMargin

    val optimizedSil =
      """|sil @test0 : $@convention(thin) () -> (Builtin.Int8, Builtin.Int8, Builtin.Int16, Builtin.Int8) {
         |bb0:
         |  %0 = integer_literal $Builtin.Int8, 8
         |  %1 = integer_literal $Builtin.Int16, 8
         |  %2 = integer_literal $Builtin.Int8, 1
         |  %3 = tuple (%0 : $Builtin.Int8, %0 : $Builtin.Int8, %1 : $Builtin.Int16, %2 : $Builtin.Int8)
         |  return %3 : $(Builtin.Int8, Builtin.Int8, Builtin.Int16, Builtin.Int8)
         |}
      """.stripMargin

    val original = silFunction.parse(sil).get.value
    val expected = silFunction.parse(optimizedSil).get.value
    val optimized = CSE.run(original)

    optimized should be(expected)
  }

  optimizer should "optimize @test1 well" in {
    val sil =
      """|sil @test1 : $@convention(thin) () -> (Builtin.Int8, Builtin.Int8, Builtin.Int8) {
         |bb0:
         |    %0 = integer_literal $Builtin.Int8, 8
         |    %1 = integer_literal $Builtin.Int8, 8
         |    cond_br undef, bb1, bb2
         |
         |bb1:
         |    %2 = integer_literal $Builtin.Int8, 8
         |    br bb3(%2 : $Builtin.Int8)
         |
         |bb2:
         |    %3 = integer_literal $Builtin.Int8, 16
         |    br bb3(%3 : $Builtin.Int8)
         |
         |bb3(%4 : $Builtin.Int8):
         |    %5 = tuple(%0 : $Builtin.Int8, %4 : $Builtin.Int8, %1 : $Builtin.Int8)
         |    return %5 : $(Builtin.Int8, Builtin.Int8, Builtin.Int8)
         |}
      """.stripMargin

    val optimizedSil =
      """|sil @test1 : $@convention(thin) () -> (Builtin.Int8, Builtin.Int8, Builtin.Int8) {
         |bb0:
         |  %0 = integer_literal $Builtin.Int8, 8
         |  cond_br undef, bb1, bb2
         |bb1:
         |  br bb3(%0 : $Builtin.Int8)
         |bb2:
         |  %3 = integer_literal $Builtin.Int8, 16
         |  br bb3(%3 : $Builtin.Int8)
         |bb3(%5 : $Builtin.Int8):
         |  %6 = tuple (%0 : $Builtin.Int8, %5 : $Builtin.Int8, %0 : $Builtin.Int8)
         |  return %6 : $(Builtin.Int8, Builtin.Int8, Builtin.Int8)
         |}
      """.stripMargin

    val original = silFunction.parse(sil).get.value
    val expected = silFunction.parse(optimizedSil).get.value
    val optimized = CSE.run(original)

    optimized should be(expected)
  }

}
