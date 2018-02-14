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

  /*
  def immediateDominator(node: NodeT): N = {
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
  */
}
