package me.waft.sil.lang

import me.waft.sil.lang.instruction.SILTerminator

case class SILBasicBlock(label: SILLabel, instructionDefs: Seq[SILInstructionDef], terminator: SILTerminator)
