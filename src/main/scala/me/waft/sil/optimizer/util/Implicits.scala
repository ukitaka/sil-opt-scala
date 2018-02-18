package me.waft.sil.optimizer.util

import me.waft.sil.lang._

object Implicits {

  import scala.language.implicitConversions

  implicit def silBasicBlockTraverser(bb: SILBasicBlock) =
    SILBasicBlockTraverser(bb)

  implicit def silInstructionTraverser(instruction: SILInstruction) =
    SILInstructionTraverser(instruction)

  implicit def silInstructionDefTraverser(instructionDef: SILInstructionDef) =
    SILInstructionTraverser(instructionDef.instruction)

  implicit def silStatementTraverser(statement: SILStatement) =
    SILStatementTraverser(statement)

  implicit def silFunctionTraverser(function: SILFunction) =
    SILFunctionTraverser(function)
}
