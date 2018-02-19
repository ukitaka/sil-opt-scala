package me.waft.sil.optimizer.analysis.util

import org.scalatest._

import scalax.collection.GraphPredef._
import scalax.collection.immutable.Graph

class CDGSpec extends FlatSpec with Matchers {
  "Control Dependence Graph" should "be computed well from SSA program" in {
    val g = Graph(1 ~> 2, 2 ~> 5, 5 ~> 2, 2 ~> 4)
    val cdg = GraphTransformer.controlDependenceGraph(g, 1, 4, 0)
    cdg should be(Graph(1, 2, 4, 5, 2 ~> 2, 2 ~> 5))
  }

  "Control Dependence Graph" should "be computed well from SSA program 2" in {
    val g = Graph(1 ~> 2, 2 ~> 3, 2 ~> 4, 3 ~> 5, 3 ~> 6, 5 ~> 7, 6 ~> 7, 7 ~> 2)
    println(GraphTransformer.postDominatorTree(g,  4))
    val cdg = GraphTransformer.controlDependenceGraph(g, 1, 4, 0)
    cdg should be(Graph(2 ~> 2, 2 ~> 3, 2 ~> 7, 3 ~> 5, 3 ~> 6))
  }
//  "Control Dependence Graph" should "be computed well from SSA program 3" in {
//    val g = Graph(0 ~> 1, 1 ~> 2, 1 ~> 4, 2 ~> 3, 3 ~> 4)
//    val cdg = GraphTransformer.controlDependenceGraph(g, 0, 4, -1)
//    cdg should be(Graph(0, 1, 2, 3, 4, 1 ~> 2, 1 ~> 3))
//  }
}
