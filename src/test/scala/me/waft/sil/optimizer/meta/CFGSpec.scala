package me.waft.sil.optimizer.meta

import me.waft.sil.parser.SILFunctionParser
import org.scalatest._

class CFGSpec extends FlatSpec with Matchers with SILFunctionParser {
  "CFG" should "work well" in {
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
    val f = silFunction.parse(sil).get.value

    CFG(f).dumpCFG()
  }
}
