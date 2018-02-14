package me.waft.sil.optimizer.pass

import me.waft.sil.lang._
import me.waft.sil.optimizer.analysis.SILValueUsage

import scala.collection.mutable.{ Set => MutableSet }

trait DCEPass extends Pass {
  def eliminateDeadCode(function: SILFunction): SILFunction
}

// Simple dead code elimination.
// Just eliminate unused instructions.
object DCE extends DCEPass {
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

// Aggressive dead code elimination....
// Mark used basic blocks and instructions as `Live`, and
// eliminate unmarked codes.
//
object AggressiveDCE extends DCEPass {

  def mayHaveSideEffect(inst: SILInstructionDef): Boolean = inst.instruction match {
    case _ => false //TODO
  }

  def eliminateDeadCode(function: SILFunction): SILFunction = {
    val live = MutableSet[SILInstructionDef]()

    val _ = function.basicBlocks.foreach { bb =>
      bb.instructionDefs.foreach { i =>
        if (!mayHaveSideEffect(i)) {
          live.add(i)
        }
      }
    }

    def markAsLive(value: SILValue, live: MutableSet[SILInstructionDef]) = {
      ???
    }

    def definitionOfValue(value: SILValue): SILInstructionDef = {
      ???
    }

    function // TODO
  }


}
