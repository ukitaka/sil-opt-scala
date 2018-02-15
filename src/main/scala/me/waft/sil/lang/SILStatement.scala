package me.waft.sil.lang

import me.waft.sil.lang.SILStatement.{InstructionDef, Terminator}
import me.waft.sil.lang.instruction.SILTerminator

// Abstraction for SILInstructionDef and SILTerminator.
// Both of them are considered as a statement of basic block.
sealed trait SILStatement  {
  def isTerminator = this match {
    case Terminator(_) => true
    case InstructionDef(_) => false
  }

  def isInstructionDef = !isTerminator
}

object SILStatement {
  case class InstructionDef(sILInstructionDef: SILInstructionDef) extends SILStatement
  case class Terminator(terminator: SILTerminator) extends SILStatement

  def apply(instructionDef: SILInstructionDef): SILStatement = InstructionDef(instructionDef)
  def apply(terminator: SILTerminator): SILStatement = Terminator(terminator)
}
