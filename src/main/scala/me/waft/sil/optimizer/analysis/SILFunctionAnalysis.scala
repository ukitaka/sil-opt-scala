package me.waft.sil.optimizer.analysis

import me.waft.sil.lang.{Br, SILBasicBlock, SILFunction, SILLabel}
import me.waft.sil.optimizer.analysis.util.GraphTransformer

import scalax.collection.GraphPredef._
import scalax.collection.immutable.Graph

case class SILFunctionAnalysis(function: SILFunction) {
  import GraphTransformer._
  import Implicits._

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
  lazy val PDT = postDominatorTree(CFG, function.entryBB)

  // PDT(Post Dominator Tree) of `function`
  lazy val CDG = controlDependenceGraph(CFG,
                                        function.entryBB,
                                        function.canonicalExitBB,
                                        entryEmptyBB(function.entryBB))

  private[this] def entryEmptyBB(bb: SILBasicBlock): SILBasicBlock =
    SILBasicBlock(
      SILLabel("entry", bb.label.args),
      Seq(),
      Br(bb.label.identifier, bb.label.args)
    )
}
