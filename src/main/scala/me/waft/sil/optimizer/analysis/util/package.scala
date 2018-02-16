package me.waft.sil.optimizer.analysis

import scalax.collection.mutable.{Graph => MutableGraph}
import scalax.collection.{Graph, GraphBase, GraphEdge, GraphTraversal}

package object util {
  type DiGraph[N] = Graph[N, GraphEdge.DiEdge]
    with GraphBase[N, GraphEdge.DiEdge]
    with GraphTraversal[N, GraphEdge.DiEdge]
  type MutableDiGraph[N] = MutableGraph[N, GraphEdge.DiEdge]
    with GraphBase[N, GraphEdge.DiEdge]
    with GraphTraversal[N, GraphEdge.DiEdge]
}
