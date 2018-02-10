package me.waft.sil.optimizer.meta

import me.waft.sil.lang.{SILBasicBlock, SILInstructionDef, SILValue}

import scala.collection.Set
import scalax.collection.GraphPredef._
import scalax.collection.{Graph, GraphEdge}

case class SILValueUsage(bb: SILBasicBlock) {
  import Implicits._

  type UsageGraph = Graph[SILValue, GraphEdge.DiEdge]

  protected lazy val usageGraph: UsageGraph = analyseUsages(bb)

  def valueDecl(value: SILValue): SILInstructionDef =
    bb.instructionDefs.filter(_.values.contains(value)).head

  def unusedValues: Set[SILValue] =
    usageGraph.nodes
      .filter(node =>
        usageGraph.filter(usageGraph.having(edge = _.target == node)).isEmpty
        && !bb.terminator.getOperands.exists(_.value == node.value)
      )
      .map(_.value)

  private[meta] def analyseUsages(bb: SILBasicBlock): UsageGraph = {
    val nodes = bb.instructionDefs
      .flatMap(_.values)
      .map(n => Graph[SILValue, GraphEdge.DiEdge](n))
    val edges = for {
      d <- bb.instructionDefs
      user <- d.values
      uses <- d.instruction.getOperands.map(_.value)
    } yield Graph(user ~> uses)
    nodes.reduce(_ ++ _) ++ edges.reduce(_ ++ _)
  }
}
