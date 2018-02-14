package me.waft.sil.optimizer.analysis.graph

import org.scalatest._

import scalax.collection.Graph
import scalax.collection.GraphPredef._

class LengauerTarjanSpec extends FlatSpec with Matchers {
  "semiDominator" should "work well" in {
    val g = Graph(
      "r" ~> "d",
      "d" ~> "x",
      "d" ~> "s",
      "x" ~> "x2",
      "x2" ~> "y",
      "s" ~> "y",
      "s" ~> "u",
      "y" ~> "y2",
      "y2" ~> "n",
      "u" ~> "u2",
      "u2" ~> "v",
      "v" ~> "n"
    )

    val l = LengauerTarjan(g, "r")
    l.semiDominator("n").value should be("s")
  }
}
