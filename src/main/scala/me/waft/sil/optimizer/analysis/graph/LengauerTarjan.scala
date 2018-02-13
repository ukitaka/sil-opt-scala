package me.waft.sil.optimizer.analysis.graph

import scalax.collection.{Graph, GraphEdge}

case class LengauerTarjan[N](controlFlowGraph: Graph[N, GraphEdge.DiEdge], entryNodeValue: N) {

  val depthFirstSpanningTree =
    DepthFirstSpanningTree(controlFlowGraph, entryNodeValue).result

  type NodeT = depthFirstSpanningTree.NodeT

  type NodeSetT = depthFirstSpanningTree.NodeSetT

  def getNode(n: WithDFNumber[N]): NodeT =
    depthFirstSpanningTree
      .get(n)
      .asInstanceOf[NodeT]

  def getNode(n: N): NodeT = getNode(WithDFNumber(n, dfnum(n)))

  def dfnum(nodeValue: N): Int =
    depthFirstSpanningTree.nodes.find(_.nodeValue == nodeValue).get.value.number

  def dfnum(node: NodeT): Int = dfnum(node.nodeValue)

  def allPredecessors(node: NodeT): Set[NodeT] = {
    node.diPredecessors.flatMap(p => Set(p) ++ allPredecessors(p.asInstanceOf[NodeT]))
  }

  def ancestors(node: WithDFNumber[N]): Set[WithDFNumber[N]] =
    depthFirstSpanningTree.nodes
      .filter(_.value.number < node.number)
      .map(_.value)
      .toSet

  def ancestors(nodeValue: N): Set[WithDFNumber[N]] =
    ancestors(WithDFNumber(nodeValue, dfnum(nodeValue)))

  def semiDominator(nodeValue: WithDFNumber[N]): WithDFNumber[N] = {
    val node = depthFirstSpanningTree.get(nodeValue)
    semiDominator(node)
  }

  def semiDominator(node: NodeT): WithDFNumber[N] = {
    val n = node.nodeValue
    println(s"semidom(${node})")
//    println(depthFirstSpanningTree.edges)
    print("allPre = ")
    println(allPredecessors(node))

    val candidates: Set[WithDFNumber[N]] = allPredecessors(node).flatMap { v =>
      println(s"if dfnum(${v})< dfnum(${node})")
      val nodeV = depthFirstSpanningTree.get(v).value
      if (dfnum(v) < dfnum(n)) {
        println("<")
        val a = Set(depthFirstSpanningTree.get(v).value)
        println(a)
        a
      } else {
        println(">")
        val a = (ancestors(n) ++ Set(nodeV)).map(u => semiDominator(u))
        println(a)
        a
      }
    }
    println(s"candidates = ${candidates}")
    candidates.minBy(_.number)
  }

  def immediateDominator(node: NodeT): WithDFNumber[N] = {
    val semiN = semiDominator(node)
    val semiNNode: NodeT = getNode(semiN)
    val y: WithDFNumber[N] = {
      def allSuccessors(semiN: NodeT, n: NodeT): Set[NodeT] = {
        semiN.diSuccessors.filterNot(_ == n).flatMap { s =>
          Set(s) ++ allSuccessors(s.asInstanceOf[NodeT], n)
        }
      }
      allSuccessors(semiNNode, node).minBy(_.number)
    }

    if (semiDominator(y) == semiDominator(node)) {
      semiN
    } else {
      immediateDominator(getNode(y))
    }
  }
}
