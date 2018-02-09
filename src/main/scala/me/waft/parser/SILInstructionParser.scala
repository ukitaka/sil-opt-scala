package me.waft.parser

import fastparse.noApi._
import me.waft.parser.SILValueParser._
import me.waft.parser.WhiteSpaceApi._
import me.waft.sil.lang.instruction.SILInstruction
import me.waft.sil.lang.{SILInstructionDef, SILValue}

object SILInstructionParser
  extends instruction.AllocParser
    with instruction.AggregateTypesParser
    with instruction.LiteralParser
    with instruction.FunctionApplicationParser
    with instruction.AccessingMemoryParser
    with instruction.ReferenceCountingParser {

  def silInstructionResult: P[Seq[SILValue]] =
    silValue.map(name => Seq(name)) |
      silValueName.map(SILValue.apply).repTC(1).parened

  def silInstructionDefs: P[Seq[SILInstructionDef]] =
    silInstructionDef.rep(0, whitespaces)

  def silInstructionDef: P[SILInstructionDef] =
    ((silInstructionResult ~ "=").?? ~ silInstruction)
      .map(SILInstructionDef.tupled)

  def silInstruction: P[SILInstruction] =
    allocStack | allocBox | struct | structExtract | integerLiteral | builtin | projectBox | store | load |
    strongRelease
}
