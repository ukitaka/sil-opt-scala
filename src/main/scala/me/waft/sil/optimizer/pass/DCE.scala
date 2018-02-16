package me.waft.sil.optimizer.pass

import me.waft.sil.lang.{Throw, _}
import me.waft.sil.optimizer.analysis.{SILFunctionAnalysis, SILValueUsage}
import me.waft.sil.optimizer.analysis.Implicits._

import scala.collection.mutable.{Set => MutableSet}

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

// Aggressive dead code elimination.
// Mark used basic blocks and instructions as `Live`, and
// eliminate unmarked codes.
object AggressiveDCE extends DCEPass {

  def seemsUseful(statement: SILStatement): Boolean =
    statement.instruction match {
      case Return(_)   => true
      case Unreachable => true
      case Throw(_)    => true
      case _           => false
    }

  def eliminateDeadCode(function: SILFunction): SILFunction = {
    val live = MutableSet[SILStatement]()
    val analysis = SILFunctionAnalysis(function)

    function.basicBlocks.foreach { bb =>
      bb.statements.foreach { statement =>
        if (seemsUseful(statement)) {
          if (live.add(statement)) {
            propagateLiveness(statement)
          }
        }
      }
    }

    def propagateLiveness(statement: SILStatement): Unit = {
      statement.instruction.usingValues
        .map(value => function.declaredStatement(value))
        .collect { case Some(i) => i }
        .foreach { i =>
          if (live.add(i)) {
            propagateLiveness(statement)
          }
        }

      analysis.controlDependentBlocks(statement).foreach { bb =>
        if (live.add(SILStatement(bb.terminator, bb))) {
          propagateLiveness(statement)
        }
      }
    }

    def removeUnusedDefs(bb: SILBasicBlock): SILBasicBlock = {
      SILBasicBlock(
        bb.label,
        bb.instructionDefs.filter(i => live.contains(SILStatement(i, bb))),
        bb.terminator
      )
    }

    val eliminatedFunction = SILFunction(
      function.linkage,
      function.name,
      function.`type`,
      function.basicBlocks
        .map(bb => removeUnusedDefs(bb))
        .filterNot(bb => bb.instructionDefs.isEmpty && !bb.terminator.isReturn)
    )
    eliminatedFunction
  }
}

// DCE that has logic like a swift compiler uses.
// This is also similar to AggressiveDCE, but there are some differences.
// - Do not compute control dependence graph completely.
// - Do no eliminate infinity loop.
// See:
//   Optimal control dependence and the Roman chariots problem
//   TOPLAS, v19, issue 3, 1997
//   http://dx.doi.org/10.1145/256167.256217
object SwiftDCE extends DCEPass {
  def eliminateDeadCode(function: SILFunction): SILFunction = {
    val analysis = SILFunctionAnalysis(function)
    ???
  }
}