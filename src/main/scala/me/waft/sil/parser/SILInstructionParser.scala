package me.waft.sil.parser

import fastparse.noApi._
import me.waft.core.parser.Parser
import me.waft.sil.lang.instruction.SILInstruction
import me.waft.sil.lang.{SILInstructionDef, SILValue}

trait SILInstructionParser
  extends Parser
    with SILValueParser
    with instruction.AllocParser
    with instruction.AggregateTypesParser
    with instruction.LiteralParser
    with instruction.FunctionApplicationParser
    with instruction.AccessingMemoryParser
    with instruction.ReferenceCountingParser {

  import WhiteSpaceApi._

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
