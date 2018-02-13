package me.waft.sil.optimizer.analysis.graph

import scalax.collection.{Graph, GraphEdge}

case class LengauerTarjan[N](graph: Graph[N, GraphEdge.DiEdge], entryNodeValue: N) {
  type NodeT = graph.NodeT

  private lazy val depthFirstSpanningTree = DepthFirstSpanningTree(graph, entryNodeValue)

}
