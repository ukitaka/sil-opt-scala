package me.waft.sil.optimizer.pass

import me.waft.sil.lang._
import me.waft.sil.optimizer.analysis.SILFunctionAnalysis

case class CSE(function: SILFunction) {
  type Available = Map[SILInstruction, SILValue]

  lazy val analysis = SILFunctionAnalysis(function)

  def eliminateCommonSubexpression: SILFunction = ???

  def eliminate(bb: SILBasicBlock,
                available: Available): (SILBasicBlock, Available) = ???

  def eliminate(instructionDef: SILInstructionDef,
                available: Available): (Option[SILInstructionDef], Available) =
    if (available.contains(instructionDef.instruction)) {
      (None, available)
    } else {
      (Some(instructionDef),
       available ++ instructionDef.values
         .map(v => Map(instructionDef.instruction -> v))
         .reduce(_ ++ _))
    }

}

object CSE extends SILFunctionTransform {
  def run(function: SILFunction): SILFunction =
    CSE(function).eliminateCommonSubexpression
}
