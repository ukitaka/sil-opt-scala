package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.instruction.{AllocBox, AllocStack, ProjectBox}
import me.waft.sil.parser.SILOperandParser._
import me.waft.sil.parser.SILTypeParser._
import me.waft.sil.parser.WhiteSpaceApi._

trait AllocParser {
  def allocStack: P[AllocStack] = ("alloc_stack" ~ silType).map(AllocStack)
  def allocBox: P[AllocBox] = ("alloc_box" ~ silType).map(AllocBox)
  def projectBox: P[ProjectBox] = ("project_box" ~ silOperand).map(ProjectBox)
}
