package me.waft.sil.optimizer.meta

import me.waft.sil.lang.SILTerminator
import me.waft.sil.lang.instruction.SILInstruction

object Implicits {
  implicit def silTerminatorTraverse(terminator: SILTerminator) = SILTerminatorTraverse(terminator)
  implicit def silInstructionTraverse(instruction: SILInstruction) = SILInstructionTraverse(instruction)
}
