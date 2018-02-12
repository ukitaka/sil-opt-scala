package me.waft.sil.optimizer.meta

import me.waft.sil.lang.SILBasicBlock

import scalax.collection.GraphEdge
import scalax.collection.GraphPredef._
import scalax.collection.GraphTraversal.DepthFirst
import scalax.collection.immutable.Graph

class DominatorTree(cfg: CFG) {
  import Implicits._
  import scala.collection.mutable.Map

  type G = Graph[SILBasicBlock, GraphEdge.DiEdge]
  type D = Map[SILBasicBlock, Set[SILBasicBlock]]

  private var d: D = Map(cfg.entryNode.value -> Set(cfg.entryNode.value))

  cfg.entryNode
    .innerNodeTraverser
    .withKind(DepthFirst)
    .filterNot(_ == cfg.entryNode)
    .foreach { node =>
      val bb = node.value
      val predecessors: Set[SILBasicBlock] = node.diPredecessors.map(_.value)
      val unionPredecessorsD = predecessors
        .map { predecessor =>
          d.get(predecessor.value) match {
            case Some(s) => s
            case None => {
              d.update(predecessor.value, cfg.graph.nodes.map(_.value).toSet)
              d(predecessor.value)
            }
          }
        }
        .reduce(_ union _)
      d.update(bb, Set(bb) intersect unionPredecessorsD)
    }
  println(d)
}
