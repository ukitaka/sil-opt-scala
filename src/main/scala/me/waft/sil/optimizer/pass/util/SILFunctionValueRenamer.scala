package me.waft.sil.optimizer.pass.util

import me.waft.sil.lang.{SILFunction, SILValue}
import me.waft.sil.optimizer.util.Implicits._

object SILFunctionValueRenamer {
  private def isRenameable(value: SILValue): Boolean =
    value.name.matches("^%[0-9]*")

  private def valueNumber(value: SILValue): Int = value.name.tail.toInt

  def renameValues(function: SILFunction) = {
    val valueMap = function.basicBlocks.map(_.allValues)
      .map(_.filter(isRenameable).toSeq.sortBy(valueNumber))
      .flatMap(_ :+ SILValue.undef)
      .zipWithIndex
      .filterNot(_._1 == SILValue.undef)
      .map { case (value ,index) => Map(value -> SILValue.number(index)) }
      .reduce(_ ++ _)

    SILValueReplacer.replaceValuesInFunction(function) { v =>
      if (v == SILValue.undef) {
        SILValue.undef
      } else {
        valueMap(v)
      }
    }
  }
}
