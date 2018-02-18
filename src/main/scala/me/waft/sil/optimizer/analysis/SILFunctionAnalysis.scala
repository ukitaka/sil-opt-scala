package me.waft.sil.optimizer.analysis

import me.waft.sil.lang._
import me.waft.sil.optimizer.analysis.util.GraphTransformer

import scalax.collection.Graph
import scalax.collection.GraphEdge
import scalax.collection.GraphPredef._

case class SILFunctionAnalysis(function: SILFunction) {
  import GraphTransformer._
  import me.waft.sil.optimizer.util.Implicits._

  // SILValue usage
  lazy val valueUsage = SILValueUsage.from(function)

  // CFG(Control Flow Graph) of `function`
  lazy val CFG = Graph.from(
    function.basicBlocks,
    for {
      from <- function.basicBlocks
      to <- from.allBranches(function)
    } yield (from ~> to)
  )

  // PDT(Post Dominator Tree) of `function`
  lazy val PDT = postDominatorTree(CFG, function.canonicalExitBB)

  def properlyDominates(bb: SILBasicBlock, pred: SILBasicBlock): Boolean = {
    if (bb == pred) {
      false
    } else {
      PDT.get(pred).pathTo(PDT.get(bb)).isDefined
    }
  }

  // CDG(Control Dependence Graph) of `function`
  lazy val CDG = controlDependenceGraph(CFG,
                                        function.entryBB,
                                        function.canonicalExitBB,
                                        entryEmptyBB(function.entryBB))

  def controlDependentBlocks(statement: SILStatement): Set[SILBasicBlock] =
    controlDependentBlocks(statement.basicBlock)

  def controlDependentBlocks(bb: SILBasicBlock): Set[SILBasicBlock] =
    CDG.get(bb).diSuccessors.map(_.value)

  // Temporary entry node for computing Control Dependence Graph.
  private[this] def entryEmptyBB(bb: SILBasicBlock): SILBasicBlock =
    SILBasicBlock(
      SILLabel("entry", bb.label.args),
      Seq(),
      Br(bb.label.identifier, bb.label.args)
    )

  def hasInfiniteLoops: Boolean = {
    //TODO: This implementation is messy.
    function.basicBlocks.exists { bb =>
      bb.terminator match {
        case Br(label, _) => bb.label.identifier == label
        case _ => false
      }
    }
  }

  def dumpGraph(graph: Graph[SILBasicBlock, GraphEdge.DiEdge]): Unit = {
    val g = Graph.from(
      graph.nodes.map(_.value.label.identifier),
      graph.edges.map(e => e.from.value.label.identifier ~> e.to.value.label.identifier)
    )
    println(g)
  }
}
