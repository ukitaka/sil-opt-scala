package me.waft.sil.optimizer.meta

import me.waft.sil.lang._
import me.waft.sil.lang.instruction.{SILInstruction, _}

sealed trait SILTraverse

case class SILInstructionTraverse(private val instruction: SILInstruction) extends SILTraverse {
   def getValues: Seq[SILValue] = instruction match {
    case AllocStack(_) => Seq()
    case AllocBox(_) => Seq()
    case StructExtract(operand, _) => Seq(operand.value)
    case IntegerLiteral(_, _) => Seq()
    case BuiltIn(_, _, operands, _) => operands.map(_.value)
    case Struct(_, operands) => operands.map(_.value)
    case ProjectBox(operand: SILOperand) => Seq(operand.value)
    case Store(_, operand) => Seq(operand.value)
    case Load(operand) => Seq(operand.value)
    case StrongRelease(operand) => Seq(operand.value)
    case Tuple(operands) => operands.map(_.value)
    case TupleExtract(operand, _) => Seq(operand.value)
  }
}

case class SILTerminatorTraverse(private val terminator: SILTerminator) extends SILTraverse {
  def getValues: Seq[SILValue] = terminator match {
    case Unreachable => Seq()
    case Return(operand) => Seq(operand.value)
    case Throw(operand) => Seq(operand.value)
    case Unwind => Seq()
    case Br(_, operands) => operands.map(_.value)
    case CondBr(cond, _, ifTrueArgs, _, ifFalseArgs) =>
      (cond +: ifTrueArgs.map(_.value)) ++ ifFalseArgs.map(_.value)
  }
}
