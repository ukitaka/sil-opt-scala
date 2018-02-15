package me.waft.sil.optimizer.analysis

import me.waft.sil.lang.{SILBasicBlock, SILFunction, SILInstructionDef, SILValue}

import scala.collection.Set
import scalax.collection.GraphPredef._
import scalax.collection.{Graph, GraphEdge}

case class SILValueUsage(function: SILFunction) {
  import Implicits._
  import SILValueUsage._

  type UsageGraph = Graph[SILValue, GraphEdge.DiEdge]

  protected lazy val usageGraph: UsageGraph = analyseUsages(function)

  def valueDecl(value: SILValue): Option[SILInstructionDef] =
    function.basicBlocks.flatMap(bb => bb.instructionDefs).filter(_.values.contains(value)).headOption

  def unusedArgs(bb: SILBasicBlock): Set[SILValue] =
    usageGraph.nodes
      .filter(node =>
        bb.label.args.exists(_.value == node.value)
          && node.diPredecessors.isEmpty
      )
      .map(_.value)

  def unusedValues(bb: SILBasicBlock): Set[SILValue] =
    usageGraph.nodes
      .filter(node =>
        usageGraph.filter(usageGraph.having(edge = _.target == node)).isEmpty
          && !bb.label.args.exists(_.value == node.value)
          && !bb.terminator.allValues.exists(_ == node.value)
      )
      .map(_.value)

}

object SILValueUsage {
  private[analysis] def analyseUsages(function: SILFunction): Graph[SILValue, GraphEdge.DiEdge] = {
    import Implicits._
    function.basicBlocks
      .map { bb =>
        val nodes = ( bb.label.args.map(_.value) ++ bb.instructionDefs.flatMap(_.values) )
          .map(n => Graph[SILValue, GraphEdge.DiEdge](n))
        val edges = for {
          d <- bb.instructionDefs
          user <- d.values
          uses <- d.instruction.allValues
        } yield Graph(user ~> uses)
        (nodes.reduceLeftOption(_ ++ _).getOrElse(Graph())
          ++ edges.reduceLeftOption(_ ++ _).getOrElse(Graph()))
      }
    }.reduce(_ ++ _)
}
