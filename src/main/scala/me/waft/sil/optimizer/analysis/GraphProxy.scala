package me.waft.sil.optimizer.analysis

import scala.language.higherKinds
import scalax.collection.GraphPredef.EdgeLikeIn
import scalax.collection.{Graph, GraphBase, GraphEdge}

trait GraphProxy[N, E[X] <: EdgeLikeIn[X]] {
  type GraphT = Graph[N, E] with GraphBase[N, E]

  private[analysis] def graph: GraphT

  def nodes: GraphT#NodeSetT = graph.nodes

  def edges: GraphT#EdgeSetT = graph.edges
}

trait DiGraphProxy[N] extends GraphProxy[N, GraphEdge.DiEdge]
