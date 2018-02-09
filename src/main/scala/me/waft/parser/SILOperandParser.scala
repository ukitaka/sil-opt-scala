package me.waft.parser

import me.waft.parser.SILTypeParser._
import me.waft.parser.SILValueParser._
import WhiteSpaceApi._
import fastparse.noApi._
import me.waft.sil.lang.SILOperand

object SILOperandParser {
  def silOperand: P[SILOperand] = (silValue ~ ":" ~ silType).map(SILOperand.tupled)
}
