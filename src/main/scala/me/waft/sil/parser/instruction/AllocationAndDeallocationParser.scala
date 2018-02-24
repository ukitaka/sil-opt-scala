package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.{AllocBox, AllocStack, DeallocStack, ProjectBox}
import me.waft.sil.parser.SILOperandParser

trait AllocationAndDeallocationParser extends SILOperandParser {

  import WhiteSpaceApi._

  def allocStack: P[AllocStack] = ("alloc_stack" ~ silType).map(AllocStack)

  def deallocStack: P[DeallocStack] = ("dealloc_stack" ~ silOperand).map(DeallocStack)

  def allocBox: P[AllocBox] = ("alloc_box" ~ silType).map(AllocBox)

  def projectBox: P[ProjectBox] = ("project_box" ~ silOperand).map(ProjectBox)
}
