package me.waft.sil.optimizer.pass.theory

import me.waft.sil.lang.{SILBasicBlock, SILFunction, SILStatement}
import me.waft.sil.optimizer.analysis.SILValueUsage
import me.waft.sil.optimizer.pass.SILFunctionTransform

// Simple dead code elimination.
// Just eliminate unused instructions.
object DCE extends SILFunctionTransform {
  def run(function: SILFunction): SILFunction = eliminateDeadCode(function)

  def eliminateDeadCode(function: SILFunction): SILFunction = {
    val usage = SILValueUsage.from(function)
    val eliminatedFunction = SILFunction(
      function.linkage,
      function.name,
      function.`type`,
      function.basicBlocks.map(bb => eliminateDeadCodeInBB(bb, usage))
    )
    if (function == eliminatedFunction) {
      function
    } else {
      eliminateDeadCode(eliminatedFunction)
    }
  }

  def eliminateDeadCodeInBB(bb: SILBasicBlock,
                            usage: SILValueUsage): SILBasicBlock = {
    if (usage.unusedValues(bb).isEmpty) {
      return bb
    }
    removeUnusedDefs(bb, usage)
  }

  private def removeUnusedDefs(bb: SILBasicBlock,
                               usage: SILValueUsage): SILBasicBlock = {
    val unusedDefs = usage
      .unusedValues(bb)
      .flatMap(usage.function.declaredStatement)
      .collect { case SILStatement.InstructionDef(i, _) => i }
    SILBasicBlock(
      bb.label,
      bb.instructionDefs.filterNot(unusedDefs.contains),
      bb.terminator
    )
  }
}