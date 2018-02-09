package me.waft.sil.optimizer.meta

import me.waft.sil.lang.{SILBasicBlock, SILFunction, SILInstructionDef, SILValue}

import scalax.collection.{Graph, GraphEdge}

object SILValueUsage {
  type UsageGraph = Graph[SILInstructionDef, GraphEdge.DiEdge]
  def analyseUsages(bb: SILBasicBlock): UsageGraph = ???
}

