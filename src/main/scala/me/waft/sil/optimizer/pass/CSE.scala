package me.waft.sil.optimizer.pass

import me.waft.sil.lang._
import me.waft.sil.optimizer.analysis.SILFunctionAnalysis
import me.waft.sil.optimizer.rewrite.{SILFunctionValueRenamer, SILValueReplacer}
import me.waft.sil.optimizer.util.Implicits._

case class CSE(function: SILFunction) {
  type Available = Map[SILInstruction, SILValue]
  type Replace = Map[SILValue, SILValue]

  lazy val analysis = SILFunctionAnalysis(function)

  def eliminateCommonSubexpression: SILFunction = {
    val initialAvailable: Available = Map()
    val initialReplace: Replace = Map()
    val (_, replace) = function.basicBlocks.foldLeft(initialAvailable, initialReplace) {
      case ((a, r), bb) =>
        eliminate(bb, a, r)
    }
    val optimized = function
      .filterInstructionDef(i => !i.values.forall(replace.contains))
      .mapBB(bb => SILValueReplacer.replaceValuesInBasicBlock(bb)(v => replace.getOrElse(v, v)))
    SILFunctionValueRenamer.renameValues(optimized)
  }

  def eliminate(bb: SILBasicBlock,
                available: Available,
                replace: Replace): (Available, Replace) =
    bb.instructionDefs.foldLeft(available, replace) {
      case ((a, r), i) =>
        eliminate(i, a, r)
    }

  def eliminate(instructionDef: SILInstructionDef,
                available: Available,
                replace: Replace): (Available, Replace) =
    if (available.contains(instructionDef.instruction)) {
      (available,
       replace ++ instructionDef.values
         .map(_ -> available(instructionDef.instruction))
         .toMap)
    } else {
      (available ++ instructionDef.values
         .map(v => instructionDef.instruction -> v)
         .toMap,
       replace)
    }

}

object CSE extends SILFunctionTransform {
  def run(function: SILFunction): SILFunction =
    CSE(function).eliminateCommonSubexpression
}
