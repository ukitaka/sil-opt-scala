package me.waft.sil.optimizer.analysis.util

case class DominanceFrontier[N](graph: DiGraph[N], entryNodeValue: N) {

  type NodeT = graph.NodeT

  val dominatorTree = LengauerTarjan(graph, entryNodeValue).dominatorTree

  def dfLocal(n: N): Set[N] = {
    val node = graph.get(n)
    val dtNode = dominatorTree.get(n)
    node.diSuccessors
      .filterNot { s =>
        dtNode.pathTo(dominatorTree.get(s)).isDefined && s.value != dtNode.value
      }
      .map(_.value)
  }
}
