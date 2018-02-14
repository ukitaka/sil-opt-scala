package me.waft.sil.optimizer.analysis.util

import scalax.collection.{Graph, GraphEdge}
import scalax.collection.GraphPredef._

object Transform {
  def reverse[N](graph: Graph[N, GraphEdge.DiEdge]): Graph[N, GraphEdge.DiEdge] = Graph.from(
    graph.nodes,
    graph.edges.map(edge => edge.from.value ~> edge.to.value)
  )

  def dominatorTree[N](graph: Graph[N, GraphEdge.DiEdge], entryNodeValue: N): Graph[N, GraphEdge.DiEdge] =
    LengauerTarjan(graph, entryNodeValue).dominatorTree

  def postDominatorTree[N](graph: Graph[N, GraphEdge.DiEdge], exitNodeValue: N): Graph[N, GraphEdge.DiEdge] =
    dominatorTree(reverse(graph), exitNodeValue)

  def dominanceFrontier[N](graph: Graph[N, GraphEdge.DiEdge], entryNodeValue: N): Map[N, Set[N]] = {
    val df = DominanceFrontier(graph, entryNodeValue)
    graph.nodes.map(node => Map(node.value -> df.computeDF(node.value))).reduce(_ ++ _)
  }

  def postDominanceFrontier[N](graph: Graph[N, GraphEdge.DiEdge], exitNodeValue: N): Map[N, Set[N]] =
    dominanceFrontier(reverse((graph)), exitNodeValue)
}
