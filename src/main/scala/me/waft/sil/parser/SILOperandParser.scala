package me.waft.sil.parser

import fastparse.noApi._
import me.waft.core.parser.Parser
import me.waft.sil.lang.SILOperand

trait SILOperandParser extends Parser with SILTypeParser with SILValueParser {

  import WhiteSpaceApi._

  def silOperand: P[SILOperand] = (silValue ~ ":" ~ silType).map(SILOperand.tupled)
}
