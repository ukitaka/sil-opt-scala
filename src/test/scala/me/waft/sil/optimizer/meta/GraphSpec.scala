package me.waft.sil.optimizer.meta

import org.scalatest._

import scalax.collection.GraphPredef._
import scalax.collection.Graph

class GraphSpec extends FlatSpec with Matchers {
  // %1 = …
  // %2 = …
  // %3 = op %1, %2
  // %4 = op %2, %3
  "graph" should "work well" in {
    val g = Graph(3~>1, 3~>2, 4~>2, 5~>2)
    g.find(4) should be(Some(4))
    val e = g.edges.filter(e => e.source == 4).head
    e.source should be(4)
    e.target should be(2)
  }
}
