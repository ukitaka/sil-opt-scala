package me.waft.sil.optimizer.pass

import me.waft.sil.lang._
import me.waft.sil.optimizer.analysis.SILFunctionAnalysis

import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}
import scalax.collection.GraphTraversal.BreadthFirst
import me.waft.sil.optimizer.analysis.Implicits._

case class LeveledBB(bb: SILBasicBlock, level: Int)

case class ControllingInfo(self: LeveledBB,
                           predecessors: Set[LeveledBB],
                           minLevel: Int)

case class DCE(function: SILFunction) {
  type Level = Int

  val analysis = SILFunctionAnalysis(function)

  val live = MutableSet[SILStatement]()

  val CFG = analysis.CFG

  val levelMap: MutableMap[SILBasicBlock, Level] = MutableMap()

  val controllingInfoMap: MutableMap[SILBasicBlock, ControllingInfo] =
    MutableMap()

  import CFG._

  def eliminateDeadCode: SILFunction = {
    computeLevels
    computePredecessors
    markLive()

    SILFunction(
      function.linkage,
      function.name,
      function.`type`,
      function.basicBlocks
        .map(bb => removeUnusedDefs(bb))
        .filterNot(bb => bb.instructionDefs.isEmpty && !bb.terminator.isReturn)
    )
  }

  def seemsUseful(statement: SILStatement): Boolean =
    statement.instruction match {
      case Return(_)   => true
      case Unreachable => true
      case Throw(_)    => true
      case _           => false
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

  def computeLevels =
    CFG
      .get(function.entryBB)
      .innerNodeTraverser
      .withKind(BreadthFirst)
      .foreach {
        ExtendedNodeVisitor((node, _, level, _) => {
          levelMap.put(node.value, level)
        })
      }

  def computePredecessors =
    CFG
      .get(function.entryBB)
      .innerNodeTraverser
      .withKind(BreadthFirst)
      .foreach { node =>
        val leveledPredecessors =
          node.diPredecessors
            .filterNot(n => analysis.properlyDominates(node.value, n.value))
            .map(n => LeveledBB(n.value, levelMap(n.value)))
        val controllingInfo = ControllingInfo(
          LeveledBB(node.value, levelMap(node.value)),
          leveledPredecessors,
          leveledPredecessors.minBy(_.level).level
        )
        controllingInfoMap.put(node.value, controllingInfo)
      }

  def removeUnusedDefs(bb: SILBasicBlock): SILBasicBlock =
    SILBasicBlock(
      bb.label,
      bb.instructionDefs.filter(i => live.contains(SILStatement(i, bb))),
      bb.terminator
    )
}

object DCE extends SILFunctionTransform {
  def run(function: SILFunction): SILFunction = DCE(function).eliminateDeadCode
}
