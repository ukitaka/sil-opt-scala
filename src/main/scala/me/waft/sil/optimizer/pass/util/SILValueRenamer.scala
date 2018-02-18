package me.waft.sil.optimizer.pass.util

import me.waft.sil.lang._

case class SILValueRenamer(liveValues: Set[SILValue]) {
  def replaceToUndef(value: SILValue): SILValue =
    liveValues.find(_ == value).getOrElse(SILValue.undef)

  def replaceToUndef(operand: SILOperand): SILOperand =
    SILOperand(replaceToUndef(operand.value), operand.`type`)

  def replaceToUndef(terminator: SILTerminator): SILTerminator =
    terminator match {
      case Unreachable     => Unreachable
      case Return(operand) => Return(replaceToUndef(operand))
      case Throw(operand)  => Return(replaceToUndef(operand))
      case Unwind          => Unwind
      case Br(label, args) => Br(label, args.map(replaceToUndef))
      case CondBr(cond, ifTrueLabel, ifTrueArgs, ifFalseLabel, ifFalseArgs) =>
        CondBr(replaceToUndef(cond),
               ifTrueLabel,
               ifTrueArgs.map(replaceToUndef),
               ifFalseLabel,
               ifFalseArgs.map(replaceToUndef))
    }
}
