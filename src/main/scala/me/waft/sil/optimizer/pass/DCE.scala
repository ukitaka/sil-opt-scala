package me.waft.sil.optimizer.pass

import me.waft.sil.lang.{Throw, _}
import me.waft.sil.optimizer.analysis.Implicits._
import me.waft.sil.optimizer.analysis.SILFunctionAnalysis

import scala.collection.mutable.{Set => MutableSet}

case class DCE(function: SILFunction) {
  val analysis = SILFunctionAnalysis(function)
  val CFG = analysis.CFG
  val liveArgs = MutableSet[SILValue]()
  val liveStatements = MutableSet[SILStatement]()

  def seemsUseful(statement: SILStatement): Boolean =
    statement.instruction match {
      case Return(_)   => true
      case Unreachable => true
      case Throw(_)    => true
      case _           => false
    }

  def eliminateDeadCode(): SILFunction = {
    markLive()
    println(liveArgs)
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
          if (liveStatements.add(statement)) {
            propagateLiveness(statement)
          }
        }
      }
    }

  def propagateLiveness(statement: SILStatement): Unit = {
    if (liveStatements.contains(statement)) {
      return
    }
    statement.instruction.usingValues
      .map(value => (value, function.declaredStatement(value)))
      .foreach { v =>
        v match {
          case (v, None) => {
            if(liveArgs.add(v)) {
              CFG.get(statement.basicBlock).diPredecessors.map { p =>
                val s = SILStatement(p.terminator, p.value)
                if (liveStatements.add(s)) {
                  propagateLiveness(s)
                }

              }
            }
          }
          case (_, Some(i)) =>
            if (liveStatements.add(i)) {
              propagateLiveness(statement)
            }
        }
      }
    analysis.controlDependentBlocks(statement).foreach { bb =>
      if (liveStatements.add(SILStatement(bb.terminator, bb))) {
        propagateLiveness(statement)
      }
    }
  }

  def removeUnusedDefs(bb: SILBasicBlock): SILBasicBlock =
    SILBasicBlock(
      bb.label,
      bb.instructionDefs.filter(i =>
        liveStatements.contains(SILStatement(i, bb))),
      bb.terminator
    )
}

object DCE extends SILFunctionTransform {
  override def run(function: SILFunction): SILFunction =
    DCE(function).eliminateDeadCode
}
