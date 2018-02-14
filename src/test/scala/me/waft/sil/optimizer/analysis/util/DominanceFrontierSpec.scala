package me.waft.sil.optimizer.analysis.util

import org.scalatest._

import scalax.collection.Graph
import scalax.collection.GraphPredef._

class DominanceFrontierSpec extends FlatSpec with Matchers {
  "dominance frontier" should "works well" in {
    val g = Graph(4 ~> 2, 2 ~> 5, 5 ~> 2, 2 ~> 1)
    val df = DominanceFrontier(g, 4)
    df.computeDF(5) should be(Set(2))
    df.computeDF(1) should be(Set())
    df.computeDF(2) should be(Set(2))
  }
}
