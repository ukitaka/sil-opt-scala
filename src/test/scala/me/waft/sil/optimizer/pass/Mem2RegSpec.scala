package me.waft.sil.optimizer.pass

import me.waft.sil.parser.SILFunctionParser
import org.scalatest.{FlatSpec, Matchers}

class Mem2RegSpec extends FlatSpec with Matchers with SILFunctionParser {
  "@dead1" should "be optimized well" in {
    val sil =
      """|sil @place_phi : $@convention(thin) (Int64) -> Int64 {
         |bb0(%0 : $Int64):
         |  %1 = alloc_stack $Int64
         |  store %0 to %1 : $*Int64
         |  %3 = alloc_stack $Int64
         |  %4 = integer_literal $Builtin.Int64, 0
         |  %5 = struct $Int64 (%4 : $Builtin.Int64)
         |  store %5 to %3 : $*Int64
         |  %7 = integer_literal $Builtin.Int64, 3
         |  %9 = struct_extract %0 : $Int64, #Int64._value
         |  %10 = builtin "cmp_eq_Int64"(%9 : $Builtin.Int64, %7 : $Builtin.Int64) : $Builtin.Int1
         |  cond_br %10, bb1, bb2
         |bb1:
         |  %12 = struct $Int64 (%7 : $Builtin.Int64)
         |  store %12 to %3 : $*Int64
         |  br bb5
         |bb2:
         |  %15 = integer_literal $Builtin.Int64, 2
         |  %16 = builtin "cmp_eq_Int64"(%9 : $Builtin.Int64, %15 : $Builtin.Int64) : $Builtin.Int1
         |  cond_br %16, bb3, bb4
         |bb3:
         |  %18 = struct $Int64 (%15 : $Builtin.Int64)
         |  store %18 to %3 : $*Int64
         |  br bb4
         |bb4:
         |  br bb5
         |bb5:
         |  %22 = alloc_stack $Int64
         |  store %5 to %22 : $*Int64
         |  br bb6
         |bb6:
         |  %25 = struct_element_addr %22 : $*Int64, #Int64._value
         |  %26 = load %25 : $*Builtin.Int64
         |  %27 = integer_literal $Builtin.Int64, 10
         |  %29 = builtin "cmp_slt_Int64"(%26 : $Builtin.Int64, %27 : $Builtin.Int64) : $Builtin.Int1
         |  cond_br %29, bb7, bb8
         |bb7:
         |  %31 = struct_element_addr %22 : $*Int64, #Int64._value
         |  %32 = load %31 : $*Builtin.Int64
         |  %33 = integer_literal $Builtin.Int64, 1
         |  %35 = builtin "sadd_with_overflow_Int64"(%32 : $Builtin.Int64, %33 : $Builtin.Int64, %29 : $Builtin.Int1) : $(Builtin.Int64, Builtin.Int1)
         |  %36 = tuple_extract %35 : $(Builtin.Int64, Builtin.Int1), 0
         |  %37 = tuple_extract %35 : $(Builtin.Int64, Builtin.Int1), 1
         |  %38 = struct $Int64 (%36 : $Builtin.Int64)
         |  cond_fail %37 : $Builtin.Int1
         |  store %38 to %22 : $*Int64
         |  br bb6
         |bb8:
         |  %42 = load %3 : $*Int64
         |  dealloc_stack %22 : $*Int64
         |  dealloc_stack %3 : $*Int64
         |  dealloc_stack %1 : $*Int64
         |  return %42 : $Int64
         |}
      """.stripMargin

    val optimizedSil =
      """|sil @place_phi : $@convention(thin) (Int64) -> Int64 {
         |bb0(%0 : $Int64):
         |  %1 = integer_literal $Builtin.Int64, 0
         |  %2 = struct $Int64 (%1 : $Builtin.Int64)
         |  %3 = integer_literal $Builtin.Int64, 3
         |  %4 = struct_extract %0 : $Int64, #Int64._value
         |  %5 = builtin "cmp_eq_Int64"(%4 : $Builtin.Int64, %3 : $Builtin.Int64) : $Builtin.Int1
         |  cond_br %5, bb1, bb2
         |bb1:
         |  %7 = struct $Int64 (%3 : $Builtin.Int64)
         |  br bb5(%7 : $Int64)
         |bb2:
         |  %9 = integer_literal $Builtin.Int64, 2
         |  %10 = builtin "cmp_eq_Int64"(%4 : $Builtin.Int64, %9 : $Builtin.Int64) : $Builtin.Int1
         |  cond_br %10, bb3, bb4(%2 : $Int64)
         |bb3:
         |  %12 = struct $Int64 (%9 : $Builtin.Int64)
         |  br bb4(%12 : $Int64)
         |bb4(%14 : $Int64):
         |  br bb5(%14 : $Int64)
         |bb5(%16 : $Int64):
         |  br bb6(%2 : $Int64)
         |bb6(%18 : $Int64):
         |  %19 = struct_extract %18 : $Int64, #Int64._value
         |  %20 = integer_literal $Builtin.Int64, 10
         |  %21 = builtin "cmp_slt_Int64"(%19 : $Builtin.Int64, %20 : $Builtin.Int64) : $Builtin.Int1
         |  cond_br %21, bb7, bb8
         |bb7:
         |  %23 = struct_extract %18 : $Int64, #Int64._value
         |  %24 = integer_literal $Builtin.Int64, 1
         |  %25 = builtin "sadd_with_overflow_Int64"(%23 : $Builtin.Int64, %24 : $Builtin.Int64, %21 : $Builtin.Int1) : $(Builtin.Int64, Builtin.Int1)
         |  %26 = tuple_extract %25 : $(Builtin.Int64, Builtin.Int1), 0
         |  %27 = tuple_extract %25 : $(Builtin.Int64, Builtin.Int1), 1
         |  %28 = struct $Int64 (%26 : $Builtin.Int64)
         |  cond_fail %27 : $Builtin.Int1
         |  br bb6(%28 : $Int64)
         |bb8:
         |  return %16 : $Int64
         |}
      """.stripMargin

    val func0 = silFunction.parse(sil).get.value
    val func1 = silFunction.parse(optimizedSil).get.value
    //    val func2 = Mem2Reg.run(func0)
//        func1 shouldBe (func2)
  }
}
