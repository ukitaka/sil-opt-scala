package me.waft.sil.lang

import me.waft.sil.lang.decl.SILDeclRef

sealed abstract class SILInstruction(val name: String)

case class AllocStack(`type`: SILType) extends SILInstruction("alloc_stack")

case class AllocBox(`type`: SILType) extends SILInstruction("alloc_box")

case class DeallocStack(operand: SILOperand) extends SILInstruction("dealloc_stack")

case class StructExtract(operand: SILOperand, declRef: SILDeclRef)
    extends SILInstruction("struct_extract")

case class IntegerLiteral(`type`: SILType, value: Int)
    extends SILInstruction("integer_literal")

case class BuiltIn(functionName: String,
                   substitutions: Seq[SILSubstitution],
                   operands: Seq[SILOperand],
                   `type`: SILType)
    extends SILInstruction("builtin")

case class Struct(`type`: SILType, operands: Seq[SILOperand])
    extends SILInstruction("struct")

case class ProjectBox(operand: SILOperand) extends SILInstruction("project_box")

case class Store(value: SILValue, operand: SILOperand)
    extends SILInstruction("store")

case class Load(operand: SILOperand) extends SILInstruction("load")

case class DestroyAddr(operand: SILOperand) extends SILInstruction("destroy_addr")

case class StrongRelease(operand: SILOperand)
    extends SILInstruction("strong_release")

case class Tuple(operands: Seq[SILOperand]) //TODO
    extends SILInstruction("tuple")

case class TupleExtract(operand: SILOperand, index: Int)
    extends SILInstruction("tuple_extract")

case class OpenExistentialAddr(allowedAccess: SILAllowedAccess,
                               operand: SILOperand,
                               `type`: SILType)
    extends SILInstruction("open_existential_addr")

case class OpenExistentialValue(operand: SILOperand, `type`: SILType)
    extends SILInstruction("open_existential_value")

case class OpenExistentialRef(operand: SILOperand, `type`: SILType)
    extends SILInstruction("open_existential_ref")

case class OpenExistentialMetatype(operand: SILOperand, `type`: SILType)
    extends SILInstruction("open_existential_metatype")

case class DebugValue(operand: SILOperand) extends SILInstruction("debug_value")

case class DebugValueAddr(operand: SILOperand) extends SILInstruction("debug_value_addr")

case class WitnessMethod(lookupType: SILType,
                         declRef: SILDeclRef,
                         operand: SILOperand,
                         funcType: SILType)
    extends SILInstruction("witness_method")

case class Apply(noThrow: Boolean,
                 value: SILValue,
                 substitutions: Seq[SILSubstitution],
                 args: Seq[SILValue],
                 `type`: SILType)
    extends SILInstruction("apply")

sealed abstract class SILTerminator(name: String) extends SILInstruction(name) {
  def isReturn: Boolean = this match {
    case Return(_) => true
    case _         => false
  }
}

case object Unreachable extends SILTerminator("unreachable")

case class Return(operand: SILOperand) extends SILTerminator("return")

case class Throw(operand: SILOperand) extends SILTerminator("throw")

// case class Yield() extends SILTerminator

case object Unwind extends SILTerminator("unwind")

case class Br(label: String, args: Seq[SILOperand]) extends SILTerminator("br")

case class CondBr(value: SILValue,
                  ifTrueLabel: String,
                  ifTrueArgs: Seq[SILOperand],
                  ifFalseLabel: String,
                  ifFalseArgs: Seq[SILOperand])
    extends SILTerminator("cond_br")

// case class SwitchValue() extends SILTerminator
// case class SelectValue() extends SILTerminator
// case class SwitchEnum() extends SILTerminator
// case class SwitchAddr() extends SILTerminator
// case class DynamicMethodBr() extends SILTerminator
// case class CheckedCastBr() extends SILTerminator
// case class CheckedCastValueBr() extends SILTerminator
// case class CheckedCastAddrBr() extends SILTerminator
// case class TryApply() extends SILTerminator
