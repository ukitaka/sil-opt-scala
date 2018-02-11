package me.waft.sil.optimizer.meta

import me.waft.sil.lang.SILValue
import me.waft.sil.parser.SILBasicBlockParser
import org.scalatest._

import scalax.collection.Graph
import scalax.collection.GraphPredef._

class SILValueUsageSpec extends FlatSpec with Matchers with SILBasicBlockParser {
  "SIL Value usage" should "be analysed well" in {
    val sil =
      """|bb0(%0 : $Bool, %1 : $Bool):
         |  %2 = struct_extract %1 : $Bool, #Bool._value
         |  %3 = struct_extract %1 : $Bool, #Bool._value
         |  %4 = integer_literal $Builtin.Int1, -1
         |  %5 = builtin "xor_Int1"(%3 : $Builtin.Int1, %4 : $Builtin.Int1) : $Builtin.Int1
         |  %6 = struct $Bool (%5 : $Builtin.Int1)
         |  return %6 : $Bool
      """.stripMargin
    val bb = basicBlock.parse(sil).get.value
    val usage = SILValueUsage(bb)
    val graph = usage.analyseUsages(bb)

    graph should be(Graph(
      SILValue("%6") ~> SILValue("%5"),
      SILValue("%5") ~> SILValue("%4"),
      SILValue("%5") ~> SILValue("%3"),
      SILValue("%3") ~> SILValue("%1"),
      SILValue("%2") ~> SILValue("%1"),
      SILValue("%0")
    ))

    usage.unusedValues should be(Set(SILValue("%2")))
    usage.unusedArgs should be(Set(SILValue("%0")))
  }
}
