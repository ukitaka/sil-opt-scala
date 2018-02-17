package me.waft.sil.optimizer.pass

import me.waft.sil.lang.{Throw, _}
import me.waft.sil.optimizer.analysis.Implicits._
import me.waft.sil.optimizer.analysis.SILFunctionAnalysis

import scala.collection.mutable.{Set => MutableSet}

case class DCE(function: SILFunction) {
  val analysis = SILFunctionAnalysis(function)
  val live = MutableSet[SILStatement]()

  def seemsUseful(statement: SILStatement): Boolean =
    statement.instruction match {
      case Return(_) => true
      case Unreachable => true
      case Throw(_) => true
      case _ => false
    }

  def eliminateDeadCode(): SILFunction = {
    markLive()
    SILFunction(
      function.linkage,
      function.name,
      function.`type`,
      function.basicBlocks
        .map(bb => removeUnusedDefs(bb))
    )
  }

  def markLive(): Unit =
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

  def removeUnusedDefs(bb: SILBasicBlock): SILBasicBlock =
    SILBasicBlock(
      bb.label,
      bb.instructionDefs.filter(i => live.contains(SILStatement(i, bb))),
      bb.terminator
    )
}

object DCE extends SILFunctionTransform {
  override def run(function: SILFunction): SILFunction =
    DCE(function).eliminateDeadCode
}
