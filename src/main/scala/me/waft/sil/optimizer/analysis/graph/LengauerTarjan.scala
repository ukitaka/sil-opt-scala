package me.waft.sil.optimizer.analysis.graph

import scalax.collection.{Graph, GraphEdge}

case class LengauerTarjan[N](controlFlowGraph: DiGraph[N], entryNodeValue: N) {

  private val dfst: DepthFirstSpanningTree[N] =
    DepthFirstSpanningTree(controlFlowGraph, entryNodeValue)

  import dfst._

  type CFGNodeT = controlFlowGraph.NodeT
  type CFGNodeSetT = controlFlowGraph.NodeSetT
  private type DFSTNodeT = depthFirstSpanningTree.NodeT
  private type DFSTNodeSetT = depthFirstSpanningTree.NodeSetT

  private def dfstNode[T <: CFGNodeT](node: T): DFSTNodeT =
    depthFirstSpanningTree.get(node).asInstanceOf[DFSTNodeT]

  private def cfgNode[T <: DFSTNodeT](node: T): CFGNodeT =
    controlFlowGraph.get(node).asInstanceOf[CFGNodeT]

  def semiDominator[T <: CFGNodeT](node: T): CFGNodeT = {
    val n = node.value
    val predecessors = node.diPredecessors

    val candidates: Set[CFGNodeT] = predecessors.flatMap { v =>
      if (dfNum(v) < dfNum(n)) {
        Set(v)
      } else {
        ancestors(dfstNode(v), false).map(a => ctfgNode(a)).map(u =>  semiDominator(u))
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
