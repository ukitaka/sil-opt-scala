package me.waft.sil.optimizer.analysis.graph

import scalax.collection.GraphTraversal.DepthFirst
import scalax.collection.GraphPredef._
import scalax.collection.mutable.{Graph => MutableGraph}
import scala.collection.mutable.{ Map => MutableMap }
import scalax.collection.immutable.Graph

case class DepthFirstSpanningTree[N](graph: DiGraph[N], entryNodeValue: N) { self =>
  import graph.ExtendedNodeVisitor

  val entryNode = graph.get(entryNodeValue)


  private val _dfNum: MutableMap[N, Int] = MutableMap()

  def dfNum: Map[N, Int] = _dfNum.toMap

  val depthFirstSpanningTree: DiGraph[N] = {
    val graph: MutableDiGraph[N] = MutableGraph()
    entryNode.innerNodeTraverser.withKind(DepthFirst).foreach {
      ExtendedNodeVisitor((node, count, _, _) => {
        if (node.hasPredecessors) {
          val predecessor = node.diPredecessors.find(n => _dfNum.contains(n.value)).get
          graph.add( predecessor.value ~> node.value )
        }
        _dfNum(node.value) = count
        graph.add(node.value)
      })
    }
    Graph.from(
      graph.nodes,
      graph.edges
    )
  }
}
