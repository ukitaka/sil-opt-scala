package me.waft.sil.optimizer.analysis

import me.waft.sil.lang.instruction.SILInstruction
import me.waft.sil.lang.{SILBasicBlock, SILInstructionDef}

object Implicits {
  import scala.language.implicitConversions
  implicit def silBasicBlockTraverse(bb: SILBasicBlock) =
    SILBasicBlockTraverse(bb)
  implicit def silInstructionTraverse(instruction: SILInstruction) =
    SILInstructionTraverse(instruction)
  implicit def silInstructionDefTraverse(instructionDef: SILInstructionDef) =
    SILInstructionTraverse(instructionDef.instruction)
}
