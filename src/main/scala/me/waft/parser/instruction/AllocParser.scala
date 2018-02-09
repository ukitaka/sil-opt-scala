package me.waft.parser.instruction

import me.waft.sil.instruction.{AllocBox, AllocStack}
import fastparse.noApi._
import me.waft.parser.WhiteSpaceApi._
import me.waft.parser.SILTypeParser._

object AllocParser {
  def allocStack: P[AllocStack] = ("alloc_stack" ~ silType).map(AllocStack)
  def allocBox: P[AllocBox] = ("alloc_box" ~ silType).map(AllocBox)
}
