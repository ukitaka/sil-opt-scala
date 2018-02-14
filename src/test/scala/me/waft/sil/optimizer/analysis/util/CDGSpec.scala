package me.waft.sil.optimizer.analysis.util

import org.scalatest._
import scalax.collection.GraphPredef._

import scalax.collection.immutable.Graph

class CDGSpec extends FlatSpec with Matchers {
  "Control Dependence Graph" should "be computed well from SSA program" in {
    val g = Graph(1 ~> 2, 2 ~> 5, 5 ~> 2, 2 ~> 4)
    val cdg = Transform.controlDependenceGraph(g, 1, 4, 0)
    cdg should be(Graph(0~>1, 0~>2, 0~>4, 2~>2, 2~>5))
  }
}
