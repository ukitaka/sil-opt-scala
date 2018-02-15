package me.waft.sil.optimizer.pass

import me.waft.sil.lang.SILValue
import me.waft.sil.parser.SILFunctionParser
import org.scalatest._

class AggressiveDCESpec extends FlatSpec with Matchers with SILFunctionParser {
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
    val func = silFunction.parse(sil).get.value
    val bb = func.basicBlocks.head
    println("------")
    val optimizedFunc = AggressiveDCE.eliminateDeadCode(func)
    println("------")
    val optimizedBb = optimizedFunc.basicBlocks.head

    // %1 is eliminated
    bb.instructionDefs.exists(_.values.contains(SILValue("%1"))) should be(true)
    optimizedBb.instructionDefs.exists(_.values.contains(SILValue("%1"))) should be(false)

    // and %0 is eliminated
    bb.instructionDefs.exists(_.values.contains(SILValue("%0"))) should be(true)
    optimizedBb.instructionDefs.exists(_.values.contains(SILValue("%0"))) should be(false)
  }
}