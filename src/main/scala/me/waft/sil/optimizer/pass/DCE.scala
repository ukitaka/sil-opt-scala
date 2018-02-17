package me.waft.sil.optimizer.pass

import me.waft.sil.lang._
import me.waft.sil.optimizer.analysis.SILFunctionAnalysis

import scala.collection.mutable.{Map => MutableMap, Set => MutableSet}
import scalax.collection.GraphTraversal.BreadthFirst

// - Do not compute control dependence graph completely.
// - Do no eliminate infinity loop.
// See:
//   Optimal control dependence and the Roman chariots problem
//   TOPLAS, v19, issue 3, 1997
//   http://dx.doi.org/10.1145/256167.256217
object DCE extends SILFunctionTransform {
  def run(function: SILFunction): SILFunction = eliminateDeadCode(function)

  def eliminateDeadCode(function: SILFunction): SILFunction = {
    val analysis = SILFunctionAnalysis(function)

    val CFG = analysis.CFG
    import CFG._

    type Level = Int
    case class LeveledBB(bb: SILBasicBlock, level: Int)
    case class ControllingInfo(self: LeveledBB, predecessors: Set[LeveledBB], minLevel: Int)

    val levelMap: MutableMap[SILBasicBlock, Level] = MutableMap()

    // compute levels
    CFG.get(function.entryBB).innerNodeTraverser.withKind(BreadthFirst)
      .foreach {
        ExtendedNodeVisitor((node, _, level, _) => {
          levelMap.put(node.value, level)
        })
      }

    val controllingInfoMap: MutableMap[SILBasicBlock, ControllingInfo] = MutableMap()

    // compute predecessors
    CFG.get(function.entryBB).innerNodeTraverser.withKind(BreadthFirst)
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

    ???
  }
}