package me.waft.sil.emitter

import me.waft.sil.lang.{SILInstruction, Unreachable, Unwind}
import me.waft.sil.lang._

object SILInstructionEmitter {
  import SILEmitter._
  //TODO: This implementation is just for debug.
  def emitSILInstruction(inst: SILInstruction): String = (inst.name + " ") + (inst match {
    case AllocStack(t) => emitSILType(t)
    case AllocBox(t) => emitSILType(t)
    case StructExtract(operand, _) => emitSILOperand(operand)
    case IntegerLiteral(_, _) => ""
    case BuiltIn(_, _, operands, _) => operands.map(emitSILOperand).mkString(", ")
    case Struct(_, operands) => operands.map(emitSILOperand).mkString(", ")
    case ProjectBox(operand) => emitSILOperand(operand)
    case Store(_, operand) => emitSILOperand(operand)
    case Load(operand) => emitSILOperand(operand)
    case StrongRelease(operand) => emitSILOperand(operand)
    case Tuple(operands) => operands.map(emitSILOperand).mkString(", ")
    case TupleExtract(operand, _) => emitSILOperand(operand)
    case Unreachable => ""
    case Return(operand) => emitSILOperand(operand)
    case Throw(operand) => emitSILOperand(operand)
    case Unwind => ""
    case Br(label, operands) => label + "(" + operands.map(emitSILOperand).mkString(", ") + ")"
    case CondBr(cond, ifTrueLabel, ifTrueArgs, ifFalseLabel, ifFalseArgs) =>
      emitSILValue(cond) + ", " +
        ifTrueLabel +
        ifTrueArgs.map(emitSILOperand).mkString("(", ", ", ")") +
        "," +
        ifFalseLabel +
        ifTrueArgs.map(emitSILOperand).mkString("(", ", ", ")")
  })

}