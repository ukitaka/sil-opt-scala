package me.waft.sil.optimizer.analysis.util

case class DominanceFrontier[N](graph: DiGraph[N], entryNodeValue: N) {

  type NodeT = graph.NodeT

  val lt = LengauerTarjan(graph, entryNodeValue)

  def dfLocal(n: N): Set[N] = {
    val node = graph.get(n)
    node.diSuccessors
      .filter { y =>
        lt.immediateDominator(y).value != n
      }
      .map(_.value)
  }

  def dfUp(n: N): Set[N] = {
    ???
  }
}
