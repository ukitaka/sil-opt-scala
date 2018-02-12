package me.waft.sil.optimizer.meta

import me.waft.sil.lang.{SILBasicBlock, SILFunction, SILTerminator}

import scalax.collection.GraphEdge
import scalax.collection.GraphPredef._
import scalax.collection.immutable.Graph

case class CFG(function: SILFunction) {
  type G = Graph[SILBasicBlock, GraphEdge.DiEdge]

  private lazy val graph: G = ???
}

