package me.waft.lang

case class SILBasicBlock(label: SILLabel, instructionDefs: Seq[SILInstructionDef], terminator: SILTerminator)
