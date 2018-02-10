package me.waft.sil.optimizer.meta

import me.waft.sil.lang.{SILInstructionDef, SILTerminator}
import me.waft.sil.lang.instruction.SILInstruction

object Implicits {
  import scala.language.implicitConversions
  implicit def silTerminatorTraverse(terminator: SILTerminator) =
    SILTerminatorTraverse(terminator)
  implicit def silInstructionTraverse(instruction: SILInstruction) =
    SILInstructionTraverse(instruction)
  implicit def silInstructionDefTraverse(instructionDef: SILInstructionDef) =
    SILInstructionTraverse(instructionDef.instruction)
}