package me.waft.sil.lang

case class SILBasicBlock(label: SILLabel, instructionDefs: Seq[SILInstructionDef], terminator: SILTerminator)
