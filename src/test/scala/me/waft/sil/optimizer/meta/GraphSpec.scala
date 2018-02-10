package me.waft.sil.optimizer.meta

import org.scalatest._

import scalax.collection.Graph
import scalax.collection.GraphPredef._

class GraphSpec extends FlatSpec with Matchers {
  // %0 = …
  // %1 = …
  // %2 = …
  // %3 = op %1, %2
  // %4 = op %2
  // %5 = op %2
  "graph" should "work well" in {
    val g = Graph(0, 3~>1, 3~>2, 4~>2, 5~>2)
    g.find(4) should be(Some(4))
    val e = g.edges.filter(e => e.source == 4).head
    e.source should be(4)
    e.target should be(2)
    g.edges.flatMap(_.privateNodes) should be(Set(1, 4, 5))
    e.adjacents should be(Set(3 ~> 2, 5 ~> 2))

    g.filter((i: Int) => i % 2 == 0) should be(Graph(0, 4 ~> 2))
    g.filter(g.having(_ == 2)) should be(Graph(2))
    g.filter(g.having(node = _ == 2)) should be(Graph(2))
    g.filter(g.having(edge = _ contains 2)) should be(Graph(3~>2, 4~>2, 5~>2))
  }
}
