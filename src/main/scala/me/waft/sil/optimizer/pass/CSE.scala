package me.waft.sil.optimizer.pass

import me.waft.sil.lang._
import me.waft.sil.optimizer.analysis.SILFunctionAnalysis
import me.waft.sil.optimizer.rewrite.SILValueReplacer

case class CSE(function: SILFunction) {
  type Available = Map[SILInstruction, SILValue]
  type Replace = Map[SILValue, SILValue]

  lazy val analysis = SILFunctionAnalysis(function)

  def eliminateCommonSubexpression: SILFunction = {
    val available: Available = Map()
    val replace: Replace = Map()
    val (_, newReplace) = function.basicBlocks.foldLeft(available, replace) {
      case ((a, r), bb) =>
        eliminate(bb, a, r)
    }
    SILFunction(
      function.linkage,
      function.name,
      function.`type`,
      function.basicBlocks
        .map { bb =>
          SILValueReplacer.replaceValuesInBasicBlock(bb)(newReplace) // TODO: remove unused statements
        }
    )
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
