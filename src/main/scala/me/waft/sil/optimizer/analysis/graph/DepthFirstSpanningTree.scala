package me.waft.sil.optimizer.analysis.graph

import scalax.collection.GraphTraversal.DepthFirst
import scalax.collection.GraphPredef._
import scalax.collection.mutable.{Graph => MutableGraph}
import scala.collection.mutable.Map
import scalax.collection.immutable.Graph

case class WithDFNumber[N](nodeValue: N, number: Int)

case class DepthFirstSpanningTree[N](graph: DiGraph[N], entryNodeValue: N) {
  import graph.ExtendedNodeVisitor

  private val depthFirstSpanningTree: MutableDiGraph[WithDFNumber[N]] = MutableGraph()

  private val entryNode = graph.get(entryNodeValue)

  private val dfnum: Map[N, Int] = Map()

  entryNode.innerNodeTraverser.withKind(DepthFirst).foreach {
    ExtendedNodeVisitor((node, count, _, _) => {
      if (node.hasPredecessors) {
        val predecessor = node.diPredecessors.find(n => dfnum.contains(n.value)).get
        depthFirstSpanningTree.add(
          WithDFNumber(predecessor.value, dfnum(predecessor)) ~> WithDFNumber(node.value, count)
        )
      }
      dfnum(node.value) = count
      depthFirstSpanningTree.add(WithDFNumber(node.value, count))
    })
  }

  lazy val result: DiGraph[WithDFNumber[N]] = Graph.from(
    depthFirstSpanningTree.nodes,
    depthFirstSpanningTree.edges,
  )
}
