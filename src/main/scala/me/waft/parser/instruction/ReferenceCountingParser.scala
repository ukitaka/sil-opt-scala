package me.waft.parser.instruction

import me.waft.sil.instruction.StrongRelease
import fastparse.noApi._
import me.waft.parser.WhiteSpaceApi._
import me.waft.parser.SILOperandParser._

trait ReferenceCountingParser {
  def strongRelease: P[StrongRelease] = ("strong_release" ~ silOperand).map(StrongRelease)
}
