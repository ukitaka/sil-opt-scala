package me.waft.sil.optimizer.analysis

import scalax.collection.{Graph, GraphBase, GraphEdge, GraphTraversal}

package object graph {
  type DiGraph[N] = Graph[N, GraphEdge.DiEdge]
    with GraphBase[N, GraphEdge.DiEdge]
    with GraphTraversal[N, GraphEdge.DiEdge]
}
