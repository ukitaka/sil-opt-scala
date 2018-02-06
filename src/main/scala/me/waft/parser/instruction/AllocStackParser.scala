package me.waft.parser.instruction

import me.waft.sil.instruction.AllocStack
import fastparse.noApi._
import me.waft.parser.White._
import me.waft.parser.SILTypeParser._

object AllocStackParser {
  def allocStack: P[AllocStack] = ("alloc_stack" ~ silType).map(AllocStack)
}
