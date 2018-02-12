package me.waft.sil.optimizer.meta

import scalax.collection.{Graph, GraphBase, GraphEdge}
import scalax.collection.GraphPredef.EdgeLikeIn
import scala.language.higherKinds

trait GraphProxy[N, E[X] <: EdgeLikeIn[X]] {
  type GraphT = Graph[N, E] with GraphBase[N, E]

  private[meta] def graph: GraphT

  def nodes: GraphT#NodeSetT = graph.nodes

  def edges: GraphT#EdgeSetT = graph.edges
}

trait DiGraphProxy[N] extends GraphProxy[N, GraphEdge.DiEdge]
