package me.waft.sil.optimizer.meta

import me.waft.sil.lang.SILOperand
import me.waft.sil.lang.instruction.SILInstruction
import me.waft.sil.lang.instruction._

object SILTraverse {
  def getOperands(sILInstruction: SILInstruction): Seq[SILOperand] = sILInstruction match {
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
