package me.waft.sil.instruction

import me.waft.sil.decl.SILDeclRef
import me.waft.sil.{SILOperand, SILType}
import me.waft.swift.`type`.SwiftType

sealed abstract class SILInstruction(name: String)

case class AllocStack(`type`: SILType) extends SILInstruction("alloc_stack")

case class AllocBox(`type`: SILType) extends SILInstruction("alloc_box")

case class StructExtract(operand: SILOperand, declRef: SILDeclRef)
  extends SILInstruction("struct_extract")

case class IntegerLiteral(`type`: SILType, value: Int)
  extends SILInstruction("integer_literal")

case class SILSubstitution(target: SwiftType, to: SwiftType)

case class BuiltIn(name: String, substitutions: Seq[SILSubstitution], operands: Seq[SILOperand], `type`: SILType)
  extends SILInstruction("builtin")

case class Struct(`type`: SILType, operands: Seq[SILOperand])
  extends SILInstruction("struct")
