package me.waft.sil.optimizer.pass

import me.waft.sil.lang._

import scala.collection.Set

object DCE extends Pass {
  def eliminateDeadCode(function: SILFunction) =
    SILFunction(
      function.linkage,
      function.name,
      function.`type`,
      function.basicBlocks.map(eliminateDeadCodeInBB)
    )

  def eliminateDeadCodeInBB(bb: SILBasicBlock): SILBasicBlock = {
    val usage = SILValueUsage(bb)
    if (usage.unusedValues.isEmpty) {
      return bb
    }
    val unusedDefs = usage.unusedValues.flatMap(usage.valueDecl)
    eliminateDeadCodeInBB(removeUnusedDefs(bb, unusedDefs))
  }

  private def removeUnusedDefs(bb: SILBasicBlock, unusedDefs: Set[SILInstructionDef]) =
    SILBasicBlock(
      bb.label,
      bb.instructionDefs.filterNot(unusedDefs.contains),
      bb.terminator
    )
}
