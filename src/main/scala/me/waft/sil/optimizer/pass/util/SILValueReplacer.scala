package me.waft.sil.optimizer.pass.util

import me.waft.sil.lang._

object SILValueReplacer {
  def replaceValuesInOperand(operand: SILOperand)(f: SILValue => SILValue): SILOperand =
    SILOperand(f(operand.value), operand.`type`)

  def replaceValuesInInstruction(inst: SILInstruction)(f: SILValue => SILValue): SILInstruction = {
    val r = { (op: SILOperand) => replaceValuesInOperand(op)(f) }
    inst match {
      case AllocStack(_)                   => inst
      case AllocBox(_)                     => inst
      case StructExtract(operand, declRef) => StructExtract(r(operand), declRef)
      case IntegerLiteral(_, _)            => inst
      case BuiltIn(f, s, operands, t)      => BuiltIn(f, s, operands.map(r), t)
      case Struct(t, operands)             => Struct(t, operands.map(r))
      case ProjectBox(operand)             => ProjectBox(r(operand))
      case Store(v, operand)               => Store(f(v), r(operand))
      case Load(operand)                   => Load(r(operand))
      case StrongRelease(operand)          => StrongRelease(r(operand))
      case Tuple(operands)                 => Tuple(operands.map(r))
      case TupleExtract(operand, i)        => TupleExtract(r(operand), i)
      case Unreachable     => Unreachable
      case Return(operand) => Return(r(operand))
      case Throw(operand)  => Return(r(operand))
      case Unwind          => Unwind
      case Br(label, args) => Br(label, args.map(r))
      case CondBr(cond, ifTrueLabel, ifTrueArgs, ifFalseLabel, ifFalseArgs) =>
        CondBr(f(cond),
          ifTrueLabel,
          ifTrueArgs.map(r),
          ifFalseLabel,
          ifFalseArgs.map(r))
    }
  }

  def replaceValuesInTerminator(terminator: SILTerminator)(f: SILValue => SILValue): SILTerminator =
    replaceValuesInInstruction(terminator)(f).asInstanceOf[SILTerminator]

  def replaceValueInInstructionDef(instDef: SILInstructionDef)(f: SILValue => SILValue): SILInstructionDef =
    SILInstructionDef(instDef.values.map(f), replaceValuesInInstruction(instDef.instruction)(f))

  def replaceValuesInLabel(label: SILLabel)(f: SILValue => SILValue): SILLabel = {
    val r = { (op: SILOperand) => replaceValuesInOperand(op)(f) }
    SILLabel(label.identifier, label.args.map(r))
  }

  def replaceValuesInBasicBlock(bb: SILBasicBlock)(f: SILValue => SILValue) = SILBasicBlock(
    replaceValuesInLabel(bb.label)(f),
    bb.instructionDefs.map(i => replaceValueInInstructionDef(i)(f)),
    replaceValuesInTerminator(bb.terminator)(f)
  )

  def replaceValuesInFunction(function: SILFunction)(f: SILValue => SILValue) = SILFunction(
    function.linkage,
    function.name,
    function.`type`,
    function.basicBlocks.map(bb => replaceValuesInBasicBlock(bb)(f))
  )
}