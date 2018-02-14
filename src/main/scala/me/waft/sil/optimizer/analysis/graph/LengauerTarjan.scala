package me.waft.sil.optimizer.analysis.graph

case class LengauerTarjan[N](controlFlowGraph: DiGraph[N], entryNodeValue: N) {

  val dfst: DepthFirstSpanningTree[N] =
    DepthFirstSpanningTree(controlFlowGraph, entryNodeValue)


  import dfst._

  type CFGNodeT = controlFlowGraph.NodeT
  type CFGNodeSetT = controlFlowGraph.NodeSetT
  type DFSTNodeT = depthFirstSpanningTree.NodeT
  type DFSTNodeSetT = depthFirstSpanningTree.NodeSetT

  def dfstNode[T <: CFGNodeT](node: T): DFSTNodeT =
    depthFirstSpanningTree.get(node.value).asInstanceOf[DFSTNodeT]

  def dfstNode(nodeValue: N): DFSTNodeT =
    depthFirstSpanningTree.get(nodeValue).asInstanceOf[DFSTNodeT]

  def cfgNode[T <: DFSTNodeT](node: T): CFGNodeT =
    controlFlowGraph.get(node.value).asInstanceOf[CFGNodeT]

  def cfgNode(nodeValue: N): CFGNodeT =
    controlFlowGraph.get(nodeValue).asInstanceOf[CFGNodeT]

  private def ancestors[T <: DFSTNodeT](node: T, proper: Boolean): Set[DFSTNodeT] = {
    val nodes = depthFirstSpanningTree.nodes
      .filter(n => n.pathTo(node).isDefined && dfNum(n) < dfNum(node))
      .map(_.asInstanceOf[DFSTNodeT])
      .toSet
    if (proper) nodes else (Set(node.asInstanceOf[DFSTNodeT]) ++ nodes)
  }

  /**
    * Semidominator Theorem
    *
    * To find the semidominator of a node n, consider all predecessors v of n in the CFG
    * ・If v is a proper ancestor of n in the spanning tree, then v is a candidate for semi(n)
    * ・If v is a non-ancestor of n then for each u that is an ancestor of v (or u = v), let semi(u) be a candidate for semi(n)
    *
    * NOTE: This theorem is described in "Modern Compiler Implementation in ML.
    * But this definition misses some conditions. I think "dfnum(u) > dfnum(n)" is necessary for the latter.
    */
  def semiDominator(nodeValue: N): CFGNodeT = semiDominator(cfgNode(nodeValue))

  def semiDominator[T <: CFGNodeT](node: T): CFGNodeT = {
    val n = node.value
    val predecessors = node.diPredecessors

    val candidates: Set[CFGNodeT] = predecessors.flatMap { v =>
      if (dfNum(v) < dfNum(n)) {
        Set(v)
      } else {
        ancestors(dfstNode(v), false)
          .filter(u => dfNum(u) > dfNum(n))
          .map(a => cfgNode(a))
          .map(u =>  semiDominator(u))
      }
    }
    candidates.minBy(node => dfNum(node.value))
  }

  /**
    * Dominator Theorem
    *
    * On the spanning path below semi(n) and above or including n, let y be the node with the smallest-numbered
    * semidominator. Then idom(n) is
    *
    * ・semi(n) if semi(y) == semi(n)
    * ・idom(y) if semi(y) != semi(n)
    */
  def immediateDominator[T <: CFGNodeT](n: T): CFGNodeT = {
    val y: CFGNodeT = ???

    val semiN = semiDominator(n)

    if (semiDominator(y) == semiN) {
      semiN
    } else {
      immediateDominator(y)
    }
  }
}
