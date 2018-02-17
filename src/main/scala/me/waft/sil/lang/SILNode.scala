package me.waft.sil.lang

sealed trait SILNode {
  import SILNode._

  def basicBlock: SILBasicBlock

  def isTerminator = this match {
    case Terminator(_, _) => true
    case InstructionDef(_, _) => false
    case Argument(_, _) => false
  }

  def isInstructionDef = this match {
    case Terminator(_, _) => false
    case InstructionDef(_, _) => true
    case Argument(_, _) => false
  }

  def isArgument = this match {
    case Terminator(_, _) => false
    case InstructionDef(_, _) => false
    case Argument(_, _) => true
  }

  def instruction: SILInstruction = this match {
    case Terminator(t, _) => t
    case InstructionDef(i, _) => i.instruction
  }
}

object SILNode {

  case class InstructionDef(instructionDef: SILInstructionDef, basicBlock: SILBasicBlock) extends SILNode

  case class Terminator(terminator: SILTerminator, basicBlock: SILBasicBlock) extends SILNode

  case class Argument(operand: SILOperand, basicBlock: SILBasicBlock) extends SILNode

  def apply(instructionDef: SILInstructionDef, basicBlock: SILBasicBlock): SILNode =
    InstructionDef(instructionDef, basicBlock)

  def apply(terminator: SILTerminator, basicBlock: SILBasicBlock): SILNode =
    Terminator(terminator, basicBlock)

  def apply(arg: SILOperand, basicBlock: SILBasicBlock): SILNode =
    Argument(arg, basicBlock)
}