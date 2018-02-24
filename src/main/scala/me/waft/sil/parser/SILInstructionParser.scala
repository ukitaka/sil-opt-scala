package me.waft.sil.parser

import fastparse.noApi._
import me.waft.core.parser.Parser
import me.waft.sil.lang.{SILInstruction, SILInstructionDef, SILValue}

trait SILInstructionParser
    extends Parser
    with SILValueParser
    with instruction.AllocationAndDeallocationParser
    with instruction.AggregateTypesParser
    with instruction.LiteralParser
    with instruction.FunctionApplicationParser
    with instruction.AccessingMemoryParser
    with instruction.ProtocolParser
    with instruction.DynamicDispatchParser
    with instruction.DebugInformationParser
    with instruction.UncheckedConversionsParser
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
    allocStack | deallocStack | allocBox | struct | structExtract | integerLiteral |
      builtin | projectBox | store | load | destoryAddr |
      strongRelease | tuple | tupleExtract | functionApply |
      openExistentialAddr | openExistentialValue | openExistentialRef | openExistentialMetatype |
      classMethod | witnessMethod | debugValue | debugValueAddr | upcast
}
