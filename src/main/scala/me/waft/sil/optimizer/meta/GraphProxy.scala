package me.waft.sil.optimizer.meta

import scalax.collection.Graph
import scalax.collection.GraphPredef.EdgeLikeIn
import scala.language.higherKinds

trait GraphProxy[N, E[X] <: EdgeLikeIn[X]] {
  type GraphT = Graph[N, E]

  private[meta] def graph: GraphT

  def nodes = graph.nodes

  def edges = graph.edges
}
