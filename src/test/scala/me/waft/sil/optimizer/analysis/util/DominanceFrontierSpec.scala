package me.waft.sil.optimizer.analysis.util

import org.scalatest._

import scalax.collection.Graph
import scalax.collection.GraphPredef._

class DominanceFrontierSpec extends FlatSpec with Matchers {
  "dominance frontier" should "works well" in {
    val g = Graph(4 ~> 2, 2 ~> 5, 5 ~> 2, 2 ~> 1)
    DominanceFrontier(g, 4).computeDF(2) should be(Set(2))
  }
}
