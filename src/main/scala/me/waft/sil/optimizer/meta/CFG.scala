package me.waft.sil.optimizer.meta

import me.waft.sil.lang.{SILBasicBlock, SILFunction}

import scalax.collection.GraphEdge
import scalax.collection.GraphPredef._
import scalax.collection.immutable.Graph

case class CFG(function: SILFunction) extends GraphProxy[SILBasicBlock, GraphEdge.DiEdge] {
  import Implicits._

  private[meta] lazy val graph: GraphT = Graph.from(
    function.basicBlocks,
    for {
      from <- function.basicBlocks
      to <- from.allBranches(function)
    } yield (from ~> to)
  )

  lazy val entryNode: graph.NodeT = graph.get(function.basicBlocks.head)

  def dumpCFG() = {
    val g: Graph[String, GraphEdge.DiEdge] = Graph.from(
      graph.nodes.map(_.label.identifier),
      graph.edges.map(e => e.source.label.identifier ~> e.target.label.identifier)
    )
    println(g)
  }
}

