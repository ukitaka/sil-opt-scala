package me.waft.sil.optimizer.pass

import me.waft.sil.lang.{Throw, _}
import me.waft.sil.optimizer.analysis.SILFunctionAnalysis
import me.waft.sil.optimizer.pass.rewrite.{SILFunctionValueRenamer, SILUndefReplacer}
import me.waft.sil.optimizer.util.Implicits._

import scala.collection.mutable.{Set => MutableSet}

case class DCE(function: SILFunction) {
  val analysis = SILFunctionAnalysis(function)
  val CFG = analysis.CFG
  val liveArgs = MutableSet[SILValue]()
  val liveStatements = MutableSet[SILStatement]()
  def liveBlocks = liveStatements.map(_.basicBlock)

  def eliminateDeadCode(): SILFunction = {
    if (analysis.hasInfiniteLoops) {
      // If function has infinite loops, we cannot optimize this function.
      function
    } else {
      markLive()
      val optimized = SILFunction(
        function.linkage,
        function.name,
        function.`type`,
        function.basicBlocks
          .map(bb => removeUnusedCodes(bb))
      )
      SILFunctionValueRenamer.renameValues(optimized)
    }
  }

  def seemsUseful(statement: SILStatement): Boolean =
    statement.instruction match {
      case Return(_)   => true
      case Unreachable => true
      case Throw(_)    => true
      case _           => false
    }

  def markLive(): Unit =
    function.basicBlocks
      .flatMap(_.statements)
      .filter(seemsUseful)
      .foreach(markStatementLive)

  def markStatementLive(statement: SILStatement): Unit =
    if (liveStatements.add(statement)) {
      propagateLiveness (statement)
    }

  def markArgLive(statement: SILStatement)(arg: SILValue): Unit =
    if(liveArgs.add(arg)) {
      CFG.get(statement.basicBlock).diPredecessors.map { p =>
        markStatementLive(SILStatement(p.terminator, p.value))
      }
    }

  def propagateLiveness(statement: SILStatement): Unit = {
    // Mark statement that declares values as `live`.
    statement.instruction.usingValues
      .map(function.declaredStatement)
      .collect { case Some(s) => s }
      .foreach(markStatementLive)

    // Mark arguments that declares values as `live`, and
    // also marks predecessors's terminator `live`.
    statement.instruction.usingValues
      .filter(value => function.declaredStatement(value).isEmpty)
      .foreach(markArgLive(statement))

    // Mark block that this block is control-dependent on as `live`
    analysis.controlDependentBlocks(statement)
      .map(bb => SILStatement(bb.terminator, bb))
      .foreach(markStatementLive)
  }

  def removeUnusedCodes(bb: SILBasicBlock): SILBasicBlock =
    SILBasicBlock(
      bb.label,
      bb.instructionDefs.filter(i =>
        liveStatements.contains(SILStatement(i, bb))),
      replaceBranchWithJump(SILUndefReplacer(liveArgs.toSet).replaceToUndef(bb.terminator), nearestUsefulPostDominator(bb))
    )

  def replaceBranchWithJump(terminator: SILTerminator, block: SILBasicBlock) :SILTerminator = terminator match {
    case CondBr(_, ifTrueLabel, ifTrueArgs, ifFalseLabel, ifFalseArgs) => if (ifTrueLabel == block.label.identifier) {
      Br(ifTrueLabel, ifTrueArgs)
    } else {
      Br(ifFalseLabel, ifFalseArgs)
    }
    case _ => terminator
  }

  def nearestUsefulPostDominator(bb: SILBasicBlock): SILBasicBlock = {
    analysis.PDT.get(bb).diPredecessors.headOption.map {predecessor =>
      if (liveBlocks.contains(predecessor.value)) {
        predecessor.value
      } else {
        nearestUsefulPostDominator(predecessor.value)
      }
    }.getOrElse(bb)
  }
}

object DCE extends SILFunctionTransform {
  override def run(function: SILFunction): SILFunction =
    DCE(function).eliminateDeadCode
}
