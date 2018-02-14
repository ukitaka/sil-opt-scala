package me.waft.sil.optimizer.analysis.graph

import org.scalatest._

import scalax.collection.Graph
import scalax.collection.GraphPredef._

class DepthFirstSpanningTreeSpec extends FlatSpec with Matchers {
  "Depth first spanning tree" should "work well" in {
    val g = Graph(
      "A" ~> "B",
      "A" ~> "C",
      "B" ~> "D",
      "B" ~> "G",
      "C" ~> "E",
      "C" ~> "H",
      "D" ~> "F",
      "D" ~> "G",
      "E" ~> "C",
      "E" ~> "H",
      "F" ~> "I",
      "F" ~> "K",
      "G" ~> "J",
      "H" ~> "M",
      "H" ~> "M",
      "I" ~> "L",
      "I" ~> "L",
      "J" ~> "I",
      "K" ~> "L",
      "L" ~> "M"
    )
    val dfst = DepthFirstSpanningTree(g, "A")
    val tree = dfst.depthFirstSpanningTree
    val dfNum = dfst.dfNum
    tree.nodes.find(_.value == "A").map(n => dfNum(n.value)).get should be(1)
    tree.nodes.find(_.value == "M").map(n => dfNum(n.value)).get should (be(7) or (be(5) or be(4)))
  }
}
