package me.waft.sil.lang

sealed trait SILTerminator

case object Unreachable extends SILTerminator

case class Return(operand: SILOperand) extends SILTerminator

case class Throw(operand: SILOperand) extends SILTerminator

// case class Yield() extends SILTerminator

case object Unwind extends SILTerminator

case class Br(label: SILLabel, args: Seq[SILOperand]) extends SILTerminator

case class CondBr(condOperand: SILOperand,
                  ifTrueLabel: SILLabel, ifTrueArgs: Seq[SILOperand],
                  ifFalseLabel: SILLabel, ifFalseArgs: Seq[SILOperand]) extends SILTerminator

// case class SwitchValue() extends SILTerminator
// case class SelectValue() extends SILTerminator
// case class SwitchEnum() extends SILTerminator
// case class SwitchAddr() extends SILTerminator
// case class DynamicMethodBr() extends SILTerminator
// case class CheckedCastBr() extends SILTerminator
// case class CheckedCastValueBr() extends SILTerminator
// case class CheckedCastAddrBr() extends SILTerminator
// case class TryApply() extends SILTerminator
