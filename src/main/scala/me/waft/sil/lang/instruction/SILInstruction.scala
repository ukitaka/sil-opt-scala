package me.waft.sil.lang.instruction

import me.waft.sil.lang.decl.SILDeclRef
import me.waft.sil.lang.{SILOperand, SILSubstitution, SILType, SILValue}

sealed abstract class SILInstruction(name: String)

case class AllocStack(`type`: SILType) extends SILInstruction("alloc_stack")

case class AllocBox(`type`: SILType) extends SILInstruction("alloc_box")

case class StructExtract(operand: SILOperand, declRef: SILDeclRef)
  extends SILInstruction("struct_extract")

case class IntegerLiteral(`type`: SILType, value: Int)
  extends SILInstruction("integer_literal")

case class BuiltIn(name: String, substitutions: Seq[SILSubstitution], operands: Seq[SILOperand], `type`: SILType)
  extends SILInstruction("builtin")

case class Struct(`type`: SILType, operands: Seq[SILOperand])
  extends SILInstruction("struct")

case class ProjectBox(operand: SILOperand)
  extends SILInstruction("project_box")

case class Store(value: SILValue, operand: SILOperand)
  extends SILInstruction("store")

case class Load(operand: SILOperand)
  extends SILInstruction("load")

case class StrongRelease(operand: SILOperand)
  extends SILInstruction("strong_release")

case class TupleExtract(operand: SILOperand, index: Int)
  extends SILInstruction("tuple_extract")
