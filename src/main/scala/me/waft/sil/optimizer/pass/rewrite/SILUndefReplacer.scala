package me.waft.sil.optimizer.pass.rewrite

import me.waft.sil.lang._

// Replace unused value to undef
case class SILUndefReplacer(liveValues: Set[SILValue]) {
  def replaceToUndef(value: SILValue): SILValue =
    liveValues.find(_ == value).getOrElse(SILValue.undef)

  def replaceToUndef(operand: SILOperand): SILOperand =
    SILValueReplacer.replaceValuesInOperand(operand)(replaceToUndef)

  def replaceToUndef(terminator: SILTerminator): SILTerminator =
    if (terminator.isReturn) terminator
    else
      SILValueReplacer.replaceValuesInTerminator(terminator)(replaceToUndef)
}
