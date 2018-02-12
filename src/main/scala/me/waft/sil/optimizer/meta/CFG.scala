package me.waft.sil.optimizer.meta

import me.waft.sil.lang.{SILBasicBlock, SILFunction}

import scalax.collection.GraphEdge
import scalax.collection.GraphPredef._
import scalax.collection.immutable.Graph

case class CFG(function: SILFunction) {
  import Implicits._
  type G = Graph[SILBasicBlock, GraphEdge.DiEdge]

  private lazy val graph: G = Graph.from(
    function.basicBlocks,
    for {
      from <- function.basicBlocks
      to <- from.allBranches(function)
    } yield (from ~> to)
  )

  def dumpCFG() = {
    val g: Graph[String, GraphEdge.DiEdge] = Graph.from(
      graph.nodes.map(_.label.identifier),
      graph.edges.map(e => e.source.label.identifier ~> e.target.label.identifier)
    )
    println(g)
  }
}

