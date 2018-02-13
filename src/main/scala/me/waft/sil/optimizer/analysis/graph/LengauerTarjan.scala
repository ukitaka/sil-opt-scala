package me.waft.sil.optimizer.analysis.graph

import scalax.collection.{Graph, GraphEdge}

case class LengauerTarjan[N](controlFlowGraph: Graph[N, GraphEdge.DiEdge], entryNodeValue: N) {

  val depthFirstSpanningTree =
    DepthFirstSpanningTree(controlFlowGraph, entryNodeValue).result

  type NodeT = depthFirstSpanningTree.NodeT

  type NodeSetT = depthFirstSpanningTree.NodeSetT

  def dfnum(nodeValue: N): Int =
    depthFirstSpanningTree.nodes.find(_.nodeValue == nodeValue).get.value.number

  def dfnum(node: NodeT): Int = dfnum(node.nodeValue)

  def allPredecessors(node: NodeT): Set[NodeT] =
    if(node.hasPredecessors) {
      node.diPredecessors
        .flatMap(p => allPredecessors(p.asInstanceOf[NodeT]))
    } else {
      Set()
    }

  def ancestors(node: WithDFNumber[N]): Set[WithDFNumber[N]] =
    depthFirstSpanningTree.nodes
      .filter(_.value.number < node.number)
      .map(_.value)
      .toSet

  def ancestors(nodeValue: N): Set[WithDFNumber[N]] =
    ancestors(WithDFNumber(nodeValue, dfnum(nodeValue)))

  def semidominator(nodeValue: WithDFNumber[N]): WithDFNumber[N] = {
    val node = depthFirstSpanningTree.get(nodeValue)
    semidominator(node)
  }

  def semidominator(node: NodeT): WithDFNumber[N] = {
    val n = node.nodeValue
    val candidates: Set[WithDFNumber[N]] = allPredecessors(node).flatMap { v =>
      if (dfnum(v) < dfnum(n)) {
        Set(depthFirstSpanningTree.get(v).value)
      } else {
        ancestors(WithDFNumber(n, dfnum(n))).map(u => semidominator(u))
      }
    }
    candidates.minBy(_.number)
  }
}
