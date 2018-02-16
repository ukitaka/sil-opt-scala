package me.waft.sil.optimizer.analysis.util

import org.scalatest._

import scalax.collection.Graph
import scalax.collection.GraphPredef._

class LengauerTarjanSpec extends FlatSpec with Matchers {
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

  "semiDominator" should "work well" in {
    l.semiDominator("n").value should (be("s") or be("d"))
  }

  "immediateDominator" should "work well" in {
    l.immediateDominator("s").value should (be("d"))
    l.immediateDominator("n").value should (be("d"))
  }

  val dt = l.dominatorTree

  "dominator tree" should "be spanning tree" in {
    g.nodes.forall(node => dt.nodes.contains(node)) should be(true)
  }

  "dominator tree" should "include edge that starts from root" in {
    dt.edges.contains("r" ~> "d") should be(true)
  }
}
