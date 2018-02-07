package me.waft.sil.instruction

import me.waft.sil.{SILOperand, SILType}

sealed abstract class SILInstruction(name: String)

case class AllocStack(`type`: SILType) extends SILInstruction("alloc_stack")

case class AllocBox(`type`: SILType) extends SILInstruction("alloc_box")

case class StructExtract(operand: SILOperand) extends SILInstruction("struct_extract")