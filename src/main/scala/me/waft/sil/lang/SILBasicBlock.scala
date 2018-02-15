package me.waft.sil.lang

import me.waft.sil.lang.instruction.{Br, SILTerminator}

case class SILBasicBlock(label: SILLabel, instructionDefs: Seq[SILInstructionDef], terminator: SILTerminator)

object SILBasicBlock {
  def empty(entryBB: SILBasicBlock) = SILBasicBlock(
    SILLabel("entry", entryBB.label.args),
    Seq(),
    Br(entryBB.label.identifier, entryBB.label.args)
  )
}
