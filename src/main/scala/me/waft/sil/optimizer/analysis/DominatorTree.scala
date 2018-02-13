package me.waft.sil.optimizer.analysis

import me.waft.sil.lang.SILBasicBlock

import scala.annotation.tailrec
import scalax.collection.{Graph, GraphEdge}
import scalax.collection.GraphPredef._
import scalax.collection.GraphTraversal.DepthFirst

case class DominatorTree(cfg: CFG) extends DiGraphProxy[SILBasicBlock] {
  import scala.collection.mutable.Map

  type NodeT = GraphT#NodeT

  type DominatorMap = Map[SILBasicBlock, Set[SILBasicBlock]]

  private lazy val dominatorMap: DominatorMap = buildDominatorMap

  private def buildDominatorMap: DominatorMap = {
    val initialDominatorMap: DominatorMap =
        Map(cfg.entryNode.value -> Set(cfg.entryNode.value)) ++
          Map(cfg.nodes.filterNot((node: NodeT) => node == cfg.entryNode)
            .map(_.value -> cfg.nodes.map((node: NodeT) => node.value).toSet).toSeq: _*)

    def predecessorsDominatorMap(node: NodeT, current: DominatorMap): Set[SILBasicBlock] =
      node.diPredecessors
        .map(_.value)
        .map(bb => current(bb))
        .reduce(_ intersect  _)

    def update(node: NodeT, current: DominatorMap): Boolean = {
      val newValue = predecessorsDominatorMap(node, current) union Set(node.value)
      if (newValue == current(node.value)) {
        return false
      } else {
        current.update(node.value, newValue)
        return true
      }
    }

    @tailrec
    def build(current: DominatorMap): DominatorMap = {
      val updated = cfg.entryNode
        .innerNodeTraverser
        .withKind(DepthFirst)
        .filterNot(_ == cfg.entryNode)
        .map { node => update(node, current) }
        .reduce(_ || _)

      if (updated) {
        current
      } else {
        build(current)
      }
    }

    build(initialDominatorMap)
  }

  private def buildDominatorTree: GraphT =
    Graph.from(
      cfg.nodes,
      for {
        (key: SILBasicBlock, values: Set[SILBasicBlock]) <- dominatorMap
        value <- values
      } yield (value ~> key)
    )

  // graph that represents dominator tree
  override private[analysis] lazy val graph = buildDominatorTree


  def dumpDT() = {
    val g: Graph[String, GraphEdge.DiEdge] = Graph.from(
      graph.nodes.map(_.label.identifier),
      graph.edges.map(e => e.source.label.identifier ~> e.target.label.identifier)
    )
    println(g)
  }
}