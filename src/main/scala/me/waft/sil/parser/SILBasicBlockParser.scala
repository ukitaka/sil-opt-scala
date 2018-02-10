package me.waft.sil.parser

import fastparse.noApi._
import me.waft.core.parser.Parser
import me.waft.sil.lang.SILBasicBlock

trait SILBasicBlockParser extends Parser
  with SILInstructionParser
  with SILTerminatorParser
{
  import WhiteSpaceApi._

  def basicBlock: P[SILBasicBlock] =
    (silLabel ~ silInstructionDefs ~ silTerminator).map(SILBasicBlock.tupled)
}
