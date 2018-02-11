package me.waft.sil.optimizer.meta

import me.waft.sil.lang.{SILBasicBlock, SILInstructionDef, SILValue}

import scala.collection.Set
import scalax.collection.GraphPredef._
import scalax.collection.{Graph, GraphEdge}

case class SILValueUsage(bb: SILBasicBlock) {
  import Implicits._

  type UsageGraph = Graph[SILValue, GraphEdge.DiEdge]

  protected lazy val usageGraph: UsageGraph = analyseUsages(bb)

  def valueDecl(value: SILValue): Option[SILInstructionDef] =
    bb.instructionDefs.filter(_.values.contains(value)).headOption

  lazy val unusedArgs: Set[SILValue] =
    usageGraph.nodes
      .filter(node =>
        bb.label.args.exists(_.value == node.value)
          && usageGraph.filter(usageGraph.having(edge = _.target == node)).isEmpty
      )
      .map(_.value)

  lazy val unusedValues: Set[SILValue] =
    usageGraph.nodes
      .filter(node =>
        usageGraph.filter(usageGraph.having(edge = _.target == node)).isEmpty
          && !bb.label.args.exists(_.value == node.value)
          && !bb.terminator.getValues.exists(_ == node.value)
      )
      .map(_.value)

  private[meta] def analyseUsages(bb: SILBasicBlock): UsageGraph = {
    val nodes = ( bb.label.args.map(_.value) ++ bb.instructionDefs.flatMap(_.values) )
      .map(n => Graph[SILValue, GraphEdge.DiEdge](n))
    val edges = for {
      d <- bb.instructionDefs
      user <- d.values
      uses <- d.instruction.getValues
    } yield Graph(user ~> uses)
    (nodes.reduceLeftOption(_ ++ _).getOrElse(Graph())
        ++ edges.reduceLeftOption(_ ++ _).getOrElse(Graph()))
  }
}
