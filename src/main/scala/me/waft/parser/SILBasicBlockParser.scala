package me.waft.parser

import fastparse.noApi._
import WhiteSpaceApi._
import me.waft.parser.IdentifierParser.SIL
import me.waft.parser.SILValueParser._
import me.waft.parser.SILTypeParser._
import me.waft.parser.SILInstructionParser._
import me.waft.parser.SILTerminatorParser._
import me.waft.lang.{SILArgument, SILBasicBlock, SILLabel}


object SILBasicBlockParser {
  def basicBlock: P[SILBasicBlock] =
    (silLabel ~ silInstructionDefs ~ silTerminator).map(SILBasicBlock.tupled)

  def silLabel: P[SILLabel] =
    (SIL.identifier ~ silLabelArguments ~ ":")
      .map(SILLabel.tupled)

  private[this] def silLabelArguments: P[Seq[SILArgument]] =
    ( "(" ~ silArgument.repTC(1) ~ ")" ).??

  private[this] def silArgument: P[SILArgument] =
    (silValueName ~ ":" ~ silType)
      .map(SILArgument.tupled)
}
