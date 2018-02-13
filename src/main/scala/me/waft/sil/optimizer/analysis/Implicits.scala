package me.waft.sil.optimizer.analysis

import me.waft.sil.lang.{SILBasicBlock, SILInstructionDef, SILTerminator}
import me.waft.sil.lang.instruction.SILInstruction

object Implicits {
  import scala.language.implicitConversions
  implicit def silBasicBlockTraverse(bb: SILBasicBlock) =
    SILBasicBlockTraverse(bb)
  implicit def silTerminatorTraverse(terminator: SILTerminator) =
    SILTerminatorTraverse(terminator)
  implicit def silInstructionTraverse(instruction: SILInstruction) =
    SILInstructionTraverse(instruction)
  implicit def silInstructionDefTraverse(instructionDef: SILInstructionDef) =
    SILInstructionTraverse(instructionDef.instruction)
}
