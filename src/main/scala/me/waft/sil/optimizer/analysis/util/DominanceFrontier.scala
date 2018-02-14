package me.waft.sil.optimizer.analysis.util

case class DominanceFrontier[N](graph: DiGraph[N], entryNodeValue: N) {

  type NodeT = graph.NodeT

  private val lt = LengauerTarjan(graph, entryNodeValue)
  private val dt = lt.dominatorTree

  def computeDF(n: N): Set[N] = dfLocal(n) union dfUp(n)

  def dfLocal(n: N): Set[N] = {
    val node = graph.get(n)
    node.diSuccessors
      .filter { y =>
        lt.immediateDominator(y).value != n
      }
      .map(_.value)
  }

  def dfUp(n: N): Set[N] = {
    val node = dt.get(n)
    node.diSuccessors
      .flatMap { c =>
        val dfc = computeDF(c.value)
        dfc.filter { w =>
          // Here is described as "n does not dominates w"
          // in "Modern Compiler Implementation in ML",
          // "c does not dominates w" to be exact.
          c.pathTo(dt.get(w)).isEmpty
        }
      }
  }
}
