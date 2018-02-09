package me.waft.parser.instruction

import me.waft.lang.instruction.{AllocBox, AllocStack, ProjectBox}
import fastparse.noApi._
import me.waft.parser.WhiteSpaceApi._
import me.waft.parser.SILTypeParser._
import me.waft.parser.SILOperandParser._

trait AllocParser {
  def allocStack: P[AllocStack] = ("alloc_stack" ~ silType).map(AllocStack)
  def allocBox: P[AllocBox] = ("alloc_box" ~ silType).map(AllocBox)
  def projectBox: P[ProjectBox] = ("project_box" ~ silOperand).map(ProjectBox)
}
