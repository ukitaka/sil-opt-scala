package me.waft.sil.optimizer.pass

import me.waft.sil.lang._
import me.waft.sil.optimizer.analysis.SILFunctionAnalysis
import me.waft.sil.optimizer.util.Implicits._

case class Mem2Reg(function: SILFunction) {
  val analysis = SILFunctionAnalysis(function)

  def promote: SILFunction = {
    function.mapStatement(ss => ss)
  }

  def isWriteOnlyAllocation(value: SILValue): Boolean =
    analysis.valueUsage
      .allImmediateUsers(value)
      .forall(s =>
        s.instruction match {
          case Store(_, _)       => true
          case DeallocStack(_)   => true
          case DebugValueAddr(_) => true
      })
}

object Mem2Reg {
  def run(function: SILFunction): SILFunction = Mem2Reg(function).promote
}
