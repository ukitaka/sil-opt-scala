package me.waft.parser

import fastparse.noApi._
import me.waft.parser.SILValueParser._
import me.waft.parser.White._
import me.waft.sil.instruction.SILInstruction
import me.waft.sil.{SILInstructionDef, SILValue}

object SILInstructionParser {
  def silInstructionResult: P[Seq[SILValue]] =
    silValue.map(name => Seq(name)) |
    ("(" ~ silValueName ~ silInstructionResultNames ~ ")")
      .map(names => names._1 +: names._2)
      .map(_.map(SILValue.apply))

  private[this] def silInstructionResultNames: P[Seq[String]] =
    ("," ~ silValueName).rep(0).?.map(_.getOrElse(Seq.empty))

  def silInstructionDefs: P[Seq[SILInstructionDef]] =
    silInstructionDef.rep(0, whitespaces)
//    (silInstructionDef ~ silInstructionDefs).map(d => d._1 +: d._2) |
//      silInstructionDef.map(d => Seq(d))

  def silInstructionDef: P[SILInstructionDef] =
    ( (silInstructionResult ~ "=").?
        .map(_.getOrElse(Seq.empty)) ~
      silInstruction)
      .map(SILInstructionDef.tupled)

  import instruction.AllocParser._
  import instruction.StructParser._
  import instruction.LiteralParser._

  def silInstruction: P[SILInstruction] =
    allocStack | allocBox | structExtract | integerLiteral
}
