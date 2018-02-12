package me.waft.sil.optimizer.meta

import me.waft.sil.lang.SILBasicBlock

import scalax.collection.GraphEdge
import scalax.collection.GraphPredef._
import scalax.collection.GraphTraversal.DepthFirst
import scalax.collection.immutable.Graph

case class DominatorTree(cfg: CFG) {
  import Implicits._
  import scala.collection.mutable.Map

  type G = Graph[SILBasicBlock, GraphEdge.DiEdge]
  type D = Map[SILBasicBlock, Set[SILBasicBlock]]

  private val d: D = Map(cfg.entryNode.value -> Set(cfg.entryNode.value))

  cfg.entryNode
    .innerNodeTraverser
    .withKind(DepthFirst)
    .filterNot(_ == cfg.entryNode)
    .foreach { node =>
      val bb = node.value
      val predecessors: Set[SILBasicBlock] = node.diPredecessors.map(_.value)
      val intersectedPredecessorsD = predecessors
        .map { predecessor =>
          d.get(predecessor.value) match {
            case Some(s) => s
            case None => {
              d.update(predecessor.value, cfg.graph.nodes.map(_.value).toSet)
              d(predecessor.value)
            }
          }
        }
        .reduce(_ intersect  _)
      println(s"${node.label.identifier}'s pred is ${predecessors.map(_.label.identifier)}")
      println(s"union of ${intersectedPredecessorsD.map(_.label.identifier)} and ${Set(bb.label.identifier)} is...")
      println((Set(bb) union intersectedPredecessorsD).map(_.label.identifier))
      println("------")
      d.update(bb, Set(bb) union  intersectedPredecessorsD)
    }
  println(d.map(kv => (kv._1.label.identifier, kv._2.map(_.label.identifier))))
}
