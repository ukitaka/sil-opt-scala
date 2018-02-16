package me.waft.sil.optimizer.analysis

import me.waft.sil.lang._

case class SILInstructionTraverser(private val instruction: SILInstruction) {
  def usingValues: Set[SILValue] = instruction match {
    case AllocStack(_)                   => Set()
    case AllocBox(_)                     => Set()
    case StructExtract(operand, _)       => Set(operand.value)
    case IntegerLiteral(_, _)            => Set()
    case BuiltIn(_, _, operands, _)      => operands.map(_.value).toSet
    case Struct(_, operands)             => operands.map(_.value).toSet
    case ProjectBox(operand: SILOperand) => Set(operand.value)
    case Store(_, operand)               => Set(operand.value)
    case Load(operand)                   => Set(operand.value)
    case StrongRelease(operand)          => Set(operand.value)
    case Tuple(operands)                 => operands.map(_.value).toSet
    case TupleExtract(operand, _)        => Set(operand.value)
    case Unreachable                     => Set()
    case Return(operand)                 => Set(operand.value)
    case Throw(operand)                  => Set(operand.value)
    case Unwind                          => Set()
    case Br(_, operands)                 => operands.map(_.value).toSet
    case CondBr(cond, _, ifTrueArgs, _, ifFalseArgs) =>
      ((cond +: ifTrueArgs.map(_.value)) ++ ifFalseArgs.map(_.value)).toSet
  }
}

case class SILBasicBlockTraverser(private val bb: SILBasicBlock) {
  def allValues(): Set[SILValue] =
    bb.label.args.map(_.value).toSet ++ bb.instructionDefs
      .flatMap(_.values)
      .toSet

  def allBranches(function: SILFunction): Set[SILBasicBlock] =
    bb.terminator match {
      case Br(label, _) =>
        function.basicBlocks.filter(_.label.identifier == label).toSet
      case CondBr(_, ifTrueLabel, _, ifFalseLabel, _) =>
        (function.basicBlocks.filter(_.label.identifier == ifTrueLabel)
          ++ function.basicBlocks.filter(_.label.identifier == ifFalseLabel)).toSet
      case _ => Set()
    }
}

case class SILStatementTraverser(private val statement: SILStatement) {
  import SILStatement._

  def declaringValues: Set[SILValue] = statement match {
    case InstructionDef(inst, _) => inst.values.toSet
    case Terminator(_, _)        => Set()
  }

  def usingValues: Set[SILValue] = statement match {
    case InstructionDef(inst, _) =>
      SILInstructionTraverser(inst.instruction).usingValues
    case Terminator(terminator, _) =>
      SILInstructionTraverser(terminator).usingValues
  }
}
