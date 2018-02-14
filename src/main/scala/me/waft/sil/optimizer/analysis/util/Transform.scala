package me.waft.sil.optimizer.analysis.util

import scalax.collection.{Graph, GraphEdge}
import scalax.collection.GraphPredef._

object Transform {
  def reverse[N](graph: Graph[N, GraphEdge.DiEdge]): Graph[N, GraphEdge.DiEdge] = Graph.from(
    graph.nodes,
    graph.edges.map(edge => edge.to.value ~> edge.from.value)
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
    dominanceFrontier(reverse(graph), exitNodeValue)

  def controlDependenceGraph[N](graph: Graph[N, GraphEdge.DiEdge],
                                entryNodeValue: N,
                                exitNodeValue: N,
                                newEntryNodeValue: N,
                                newExitNodeValue:  N): Graph[N, GraphEdge.DiEdge] = {
    val newGraph: Graph[N, GraphEdge.DiEdge] = Graph.from(
      graph.nodes.map(_.value) ++ Set(newEntryNodeValue, newExitNodeValue),
      graph.edges.map(e => e.from.value ~> e.to.value) ++ Set(newEntryNodeValue ~> entryNodeValue,
        exitNodeValue ~> newExitNodeValue,
        newEntryNodeValue ~> newExitNodeValue)
    )
    val pdf = postDominanceFrontier(newGraph, newExitNodeValue)

    // CDG
    Graph.from(
      pdf.keys,
      pdf.flatMap { case (y, dfn) =>
        dfn.map(x => x ~> y)
      }
    )
  }
}
