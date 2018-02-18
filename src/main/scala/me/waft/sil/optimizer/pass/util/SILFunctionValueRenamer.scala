package me.waft.sil.optimizer.pass.util

import me.waft.sil.lang.{SILBasicBlock, SILFunction, SILValue}
import me.waft.sil.optimizer.util.Implicits._

case class SILFunctionValueRenamer(function: SILFunction) {
  private def isRenameable(value: SILValue): Boolean =
    value.name.matches("^%[0-9]*")

  private def valueNumber(value: SILValue): Int = value.name.tail.toInt

  def renameValues(function: SILFunction) = {
    val targetValues =
      function.allValues.filter(isRenameable).toSeq.sortBy(valueNumber)
    // TODO: terminator分をincrementする必要がある。
    val valueMap: Map[SILValue, SILValue] =
      ((0 until targetValues.size).map(SILValue.number) zip targetValues)
        .map(kv => Map(kv._1 -> kv._2))
        .reduce(_ ++ _)
  }
}
