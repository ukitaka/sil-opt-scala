package me.waft.sil.optimizer.pass

import me.waft.sil.lang._
import me.waft.sil.lang.instruction.{BuiltIn, Return, Throw, Unreachable}
import me.waft.sil.optimizer.analysis.SILValueUsage

import scala.collection.mutable.{Set => MutableSet}
import scala.collection.immutable.Set

trait DCEPass extends Pass {
  def eliminateDeadCode(function: SILFunction): SILFunction
}

// Simple dead code elimination.
// Just eliminate unused instructions.
object DCE extends DCEPass {
  def eliminateDeadCode(function: SILFunction) = {
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

  def eliminateDeadCodeInBB(bb: SILBasicBlock, usage: SILValueUsage): SILBasicBlock = {
    if (usage.unusedValues(bb).isEmpty) {
      return bb
    }
    removeUnusedDefs(bb, usage)
  }

  private def removeUnusedDefs(bb: SILBasicBlock, usage: SILValueUsage): SILBasicBlock = {
    val unusedDefs = usage.unusedValues(bb).flatMap(usage.valueDecl).toSet
    SILBasicBlock(
      bb.label,
      bb.instructionDefs.filterNot(unusedDefs.contains),
      bb.terminator
    )
  }
}

// Aggressive dead code elimination....
// Mark used basic blocks and instructions as `Live`, and
// eliminate unmarked codes.
//
object AggressiveDCE extends DCEPass {
  import me.waft.sil.optimizer.analysis.Implicits._

  def seemsUseful(inst: SILInstructionDef): Boolean = inst.instruction match {
    case BuiltIn(_, _, _, _) => true
    case Return(_) => true
    case Unreachable => true
    case Throw(_) => true
    case _ => false
  }

  def eliminateDeadCode(function: SILFunction): SILFunction = {
    val live = MutableSet[SILInstructionDef]()
    val usage = SILValueUsage.from(function)

    val _ = function.basicBlocks.foreach { bb =>
      bb.instructionDefs.foreach { i =>
        if (seemsUseful(i)) {
          // 関数からの戻りなど
          live.add(i)

          // 他の生きている文が使っている変数を定義する文
          i.instruction.allValues
            .map(value => usage.valueDecl(value))
            .collect { case Some(i) => i }
            .foreach { i =>
              live.add(i)
            }

          // そのブロックが制御依存しているブロックのterminator


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
