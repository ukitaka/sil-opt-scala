package me.waft.sil.optimizer.meta

import me.waft.sil.lang._
import me.waft.sil.lang.instruction.SILInstruction
import me.waft.sil.lang.instruction._

sealed trait SILTraverse

case class SILInstructionTraverse(instruction: SILInstruction) extends SILTraverse {
   def getOperands(instruction: SILInstruction): Seq[SILOperand] = instruction match {
    case AllocStack(_) => Seq()
    case AllocBox(_) => Seq()
    case StructExtract(operand, _) => Seq(operand)
    case IntegerLiteral(_, _) => Seq()
    case BuiltIn(_, _, operands, _) => operands
    case Struct(_, operands) => operands
    case ProjectBox(operand: SILOperand) => Seq(operand)
    case Store(_, operand) => Seq(operand)
    case Load(operand) => Seq(operand)
    case StrongRelease(operand) => Seq(operand)
  }
}

case class SILTerminatorTraverse(terminator: SILTerminator) extends SILTraverse {
  def getOperands: Seq[SILOperand] = terminator match {
    case Unreachable => Seq()
    case Return(operand) => Seq(operand)
    case Throw(operand) => Seq(operand)
    case Unwind => Seq()
    case Br(_, operands) => operands
    case CondBr(condOperand, _, ifTrueArgs, _, ifFalseArgs) =>
      (condOperand +: ifTrueArgs) ++ ifFalseArgs
  }
}
