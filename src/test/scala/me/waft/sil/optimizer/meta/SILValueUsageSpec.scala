package me.waft.sil.optimizer.meta

import me.waft.sil.lang.SILValue
import me.waft.sil.parser.SILBasicBlockParser
import org.scalatest._

import scalax.collection.Graph
import scalax.collection.GraphPredef._

class SILValueUsageSpec extends FlatSpec with Matchers with SILBasicBlockParser {
  "SIL Value usage" should "be analysed well" in {
    val sil =
      """|bb0(%0 : $Bool):
         |  %2 = struct_extract %0 : $Bool, #Bool._value
         |  %3 = integer_literal $Builtin.Int1, -1
         |  %4 = builtin "xor_Int1"(%2 : $Builtin.Int1, %3 : $Builtin.Int1) : $Builtin.Int1
         |  %5 = struct $Bool (%4 : $Builtin.Int1)
         |  return %5 : $Bool
      """.stripMargin
    val bb = basicBlock.parse(sil).get.value
    val graph = SILValueUsage.analyseUsages(bb)

    graph should be(Graph(
      SILValue("%5") ~> SILValue("%4"),
      SILValue("%4") ~> SILValue("%3"),
      SILValue("%4") ~> SILValue("%2"),
      SILValue("%2") ~> SILValue("%0")
    ))
  }
}
