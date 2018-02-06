package me.waft.parser

import me.waft.parser.SILTypeParser._
import me.waft.parser.SILValueParser._
import me.waft.sil.SILOperand
import White._
import fastparse.noApi._

object SILOperandParser {
  def silOperand: P[SILOperand] = (silValue ~ ":" ~ silType).map(SILOperand.tupled)
}
