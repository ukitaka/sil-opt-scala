package me.waft.sil.optimizer.meta

import me.waft.sil.lang.SILBasicBlock

import scalax.collection.GraphEdge
import scalax.collection.GraphPredef._
import scalax.collection.immutable.Graph

class DominatorTree(controlFlowGraph: CFG) {
  import Implicits._

  case class Leveled[T](value: T, level: Int)

  type G = Graph[Leveled[SILBasicBlock], GraphEdge.DiEdge]
}
