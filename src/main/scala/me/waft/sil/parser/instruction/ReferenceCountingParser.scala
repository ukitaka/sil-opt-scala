package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.StrongRelease
import me.waft.sil.parser.SILOperandParser

trait ReferenceCountingParser extends SILOperandParser {

  import WhiteSpaceApi._

  def strongRelease: P[StrongRelease] = ("strong_release" ~ silOperand).map(StrongRelease)
}
