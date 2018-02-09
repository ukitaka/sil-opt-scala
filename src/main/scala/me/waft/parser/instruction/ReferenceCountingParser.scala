package me.waft.parser.instruction

import fastparse.noApi._
import me.waft.parser.SILOperandParser._
import me.waft.parser.WhiteSpaceApi._
import me.waft.sil.lang.instruction.StrongRelease

trait ReferenceCountingParser {
  def strongRelease: P[StrongRelease] = ("strong_release" ~ silOperand).map(StrongRelease)
}
