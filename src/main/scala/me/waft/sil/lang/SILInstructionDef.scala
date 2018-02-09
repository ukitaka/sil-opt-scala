package me.waft.sil.lang

import me.waft.sil.lang.instruction.SILInstruction

case class SILInstructionDef(values: Seq[SILValue], instruction: SILInstruction)
