package me.waft.lang

import me.waft.lang.instruction.SILInstruction

case class SILInstructionDef(values: Seq[SILValue], instruction: SILInstruction)
