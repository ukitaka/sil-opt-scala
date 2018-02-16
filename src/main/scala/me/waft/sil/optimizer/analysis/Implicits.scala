package me.waft.sil.optimizer.analysis

import me.waft.sil.lang.{SILBasicBlock, SILInstruction, SILInstructionDef}

object Implicits {

  import scala.language.implicitConversions

  implicit def silBasicBlockTraverse(bb: SILBasicBlock) =
    SILBasicBlockTraverser(bb)

  implicit def silInstructionTraverse(instruction: SILInstruction) =
    SILInstructionTraverser(instruction)

  implicit def silInstructionDefTraverse(instructionDef: SILInstructionDef) =
    SILInstructionTraverser(instructionDef.instruction)
}
