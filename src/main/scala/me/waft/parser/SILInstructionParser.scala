package me.waft.parser

import fastparse.noApi._
import me.waft.parser.SILValueParser._
import me.waft.parser.WhiteSpaceApi._
import me.waft.sil.instruction.SILInstruction
import me.waft.sil.{SILInstructionDef, SILValue}

object SILInstructionParser {
  def silInstructionResult: P[Seq[SILValue]] =
    silValue.map(name => Seq(name)) |
      silValueName.map(SILValue.apply).repTC(1).parened

  def silInstructionDefs: P[Seq[SILInstructionDef]] =
    silInstructionDef.rep(0, whitespaces)

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
