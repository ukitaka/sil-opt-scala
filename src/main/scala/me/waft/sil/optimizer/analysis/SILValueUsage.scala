package me.waft.sil.optimizer.analysis

import me.waft.sil.lang._

import scala.collection.Set
import scalax.collection.{Graph, GraphEdge}
import scalax.collection.GraphPredef._
import me.waft.sil.optimizer.util.Implicits._

case class SILValueUsage(function: SILFunction,
                         usageGraph: Graph[SILValue, GraphEdge.DiEdge]) {

  def allImmediateUsers(value: SILValue): Set[SILStatement] =
    usageGraph
      .get(value)
      .diSuccessors
      .map(value => function.declaredStatement(value))
      .collect { case Some(statement) => statement }

  def unusedValues(bb: SILBasicBlock): Set[SILValue] =
    usageGraph.nodes
      .filterNot(_.hasPredecessors)
      .filterNot(_ == SILValue.undef)
      .map(_.value)
}

object SILValueUsage {
  import SILStatement._

  def from(function: SILFunction): SILValueUsage =
    SILValueUsage(function, analyseUsages(function))

  private[analysis] def analyseUsages(
      function: SILFunction): Graph[SILValue, GraphEdge.DiEdge] = {
    val nodes = function.basicBlocks.flatMap(_.allValues)

    val edges =
      for {
        statement <- function.statements
        declaredValue <- statement.declaringValues
        usingValue <- statement.usingValues
      } yield (declaredValue ~> usingValue)

    // Values in a terminator are always used by basic block.
    // Describe this by inserting undef as a predecessors of value.
    val terminatorEdges =
      for {
        terminator <- function.statements.collect { case Terminator(t, _) => t }
        usingValue <- terminator.usingValues
      } yield (SILValue.undef ~> usingValue)

    Graph.from(nodes, edges ++ terminatorEdges)
  }
}
