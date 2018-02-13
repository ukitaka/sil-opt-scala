package me.waft.sil.optimizer.analysis.graph

import scalax.collection.{Graph, GraphEdge}
import scalax.collection.GraphPredef._

object Transform {
  def reverse[N](graph: Graph[N, GraphEdge.DiEdge]): Graph[N, GraphEdge.DiEdge] = Graph.from(
    graph.nodes,
    graph.edges.map(edge => edge.from.value ~> edge.to.value)
  )
}
