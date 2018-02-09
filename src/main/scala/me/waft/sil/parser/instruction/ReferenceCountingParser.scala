package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.instruction.StrongRelease
import me.waft.sil.parser.SILOperandParser._
import me.waft.sil.parser.WhiteSpaceApi._

trait ReferenceCountingParser {
  def strongRelease: P[StrongRelease] = ("strong_release" ~ silOperand).map(StrongRelease)
}
