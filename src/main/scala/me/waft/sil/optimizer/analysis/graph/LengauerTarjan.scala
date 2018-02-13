package me.waft.sil.optimizer.analysis.graph

import scalax.collection.{Graph, GraphEdge}

case class LengauerTarjan[N](graph: Graph[N, GraphEdge.DiEdge]) {
  type NodeT = graph.NodeT
}
