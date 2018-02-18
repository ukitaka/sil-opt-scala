package me.waft.sil.lang

import me.waft.sil.lang.SILStatement.{InstructionDef, Terminator}

// Abstraction for SILInstructionDef and SILTerminator.
// Both of them are considered as a statement of basic block.
sealed trait SILStatement {
  def basicBlock: SILBasicBlock

  def isTerminator = this match {
    case Terminator(_, _) => true
    case InstructionDef(_, _) => false
  }

  def isInstructionDef = !isTerminator

  def instruction: SILInstruction = this match {
    case Terminator(t, _) => t
    case InstructionDef(i, _) => i.instruction
  }

  def declaringValues: Seq[SILValue] = this match {
    case Terminator(_, _) => Seq()
    case InstructionDef(i, _) => i.values
  }
}

object SILStatement {

  case class InstructionDef(instructionDef: SILInstructionDef, basicBlock: SILBasicBlock) extends SILStatement

  case class Terminator(terminator: SILTerminator, basicBlock: SILBasicBlock) extends SILStatement

  def apply(instructionDef: SILInstructionDef, basicBlock: SILBasicBlock): SILStatement =
    InstructionDef(instructionDef, basicBlock)

  def apply(terminator: SILTerminator, basicBlock: SILBasicBlock): SILStatement =
    Terminator(terminator, basicBlock)
}

