package me.waft.sil.optimizer.pass.util

import me.waft.sil.lang.{SILFunction, SILValue}
import me.waft.sil.optimizer.util.Implicits._

object SILFunctionValueRenamer {
  private def isRenameable(value: SILValue): Boolean =
    value.name.matches("^%[0-9]*")

  private def valueNumber(value: SILValue): Int = value.name.tail.toInt

  def renameValues(function: SILFunction) = {
    val targetValues =
      function.allValues.filter(isRenameable).toSeq.sortBy(valueNumber)
    println(s"[allvalues in ${function.name}]------")
    println(function.allValues())
    // TODO: terminator分をincrementする必要がある。
    val valueMap: Map[SILValue, SILValue] =
      ((0 until targetValues.size).map(SILValue.number) zip targetValues)
        .map(kv => Map(kv._2 -> kv._1))
        .reduce(_ ++ _)
    println(s"[valueMap in ${function.name}]------")
    println(valueMap)

    SILValueReplacer.replaceValuesInFunction(function) { v =>
      if (v == SILValue.undef) {
        SILValue.undef
      } else {
        valueMap(v)
      }
    }
  }
}
