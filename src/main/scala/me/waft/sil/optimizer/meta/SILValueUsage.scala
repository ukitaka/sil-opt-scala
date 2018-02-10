package me.waft.sil.optimizer.meta

import me.waft.sil.lang.{SILBasicBlock, SILValue}

import scalax.collection.GraphPredef._
import scalax.collection.{Graph, GraphEdge}

object SILValueUsage {
  import Implicits._

  type UsageGraph = Graph[SILValue, GraphEdge.DiEdge]
  def analyseUsages(bb: SILBasicBlock): UsageGraph = {
    val graphs = for {
      d <- bb.instructionDefs
      user <- d.values
      uses <- d.instruction.getOperands.map(_.value)
    } yield Graph(user ~> uses)
    graphs.reduce(_ ++ _)
  }
}

