package me.waft.sil.optimizer.analysis

import me.waft.sil.lang.{SILBasicBlock, SILFunction}

import scalax.collection.GraphEdge
import scalax.collection.GraphPredef._
import scalax.collection.immutable.Graph

case class CFG(function: SILFunction) extends DiGraphProxy[SILBasicBlock] {
  import Implicits._

  private[analysis] lazy val graph: GraphT = Graph.from(
    function.basicBlocks,
    for {
      from <- function.basicBlocks
      to <- from.allBranches(function)
    } yield (from ~> to)
  )

  type NodeT = GraphT#NodeT

  lazy val entryNode: NodeT = graph.get(function.basicBlocks.head)

  def dumpCFG() = {
    val g: Graph[String, GraphEdge.DiEdge] = Graph.from(
      graph.nodes.map(_.label.identifier),
      graph.edges.map(e => e.source.label.identifier ~> e.target.label.identifier)
    )
    println(g)
  }
}

