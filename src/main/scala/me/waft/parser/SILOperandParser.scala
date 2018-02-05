package me.waft.parser

import fastparse.all._
import me.waft.parser.SILTypeParser._
import me.waft.parser.SILValueParser._
import me.waft.sil.SILOperand

object SILOperandParser {
  def silOperand: P[SILOperand] = (silValue ~ silType).map(SILOperand.tupled)
}
