package me.waft.sil.emitter

import me.waft.sil.lang._
import me.waft.swift.lang.`type`._

object SILEmitter {

  import SILInstructionEmitter._

  def emitSILValue(silValue: SILValue): String = silValue.name

  //TODO
  def emitSwiftType(swiftType: SwiftType): String = swiftType match {
    case FunctionType(_, argType, _, valueType) =>
      emitSwiftType(argType) + " -> " + emitSwiftType(valueType) //TODO
    case NominalType(name) => name
    case TupleType(elements) =>
      "(" + elements.map(emitSwiftType).mkString(", ") + ")"
    case FunctionTypeArgument(_, t) => "(" + emitSwiftType(t) + ")"
    case AnnotatedType(attributes, t) =>
      attributes.map(emitSwiftTypeAttribute).mkString(" ") + emitSwiftType(t)
  }

  def emitSwiftTypeAttribute(attribute: Attribute): String =
    "@" + attribute.name + attribute.balancedTokens.mkString("(", ",", ")")

  def emitSILType(silType: SILType): String =
    "$" + emitSwiftType(silType.swiftType)

  def emitSILOperand(silOperand: SILOperand): String =
    emitSILValue(silOperand.value) + " : " + emitSILType(silOperand.`type`)

  def emitSILLabel(label: SILLabel): String =
    label.identifier + "(" + label.args.map(emitSILOperand).mkString(", ") + ")"

  def emitSILInstructionDef(inst: SILInstructionDef): String =
    inst.values.map(emitSILValue).mkString(", ") + " = " + emitSILInstruction(
      inst.instruction)

  def emitSILBasicBlock(bb: SILBasicBlock): String = {
    emitSILLabel(bb.label) + ":\n" +
      "  " + bb.instructionDefs
      .map(emitSILInstructionDef)
      .mkString("\n  ") + "\n" +
      "  " + emitSILInstruction(bb.terminator)
  }

  def emitSILLinkage(linkage: Option[SILLinkage]): String =
    linkage.map(_.name).getOrElse("")

  def emitSILFunction(func: SILFunction): String = {
    "sil " + emitSILLinkage(func.linkage) + " " + func.name + " : " + emitSILType(
      func.`type`) + " {\n" +
      func.basicBlocks.map(emitSILBasicBlock).mkString("\n") +
      "\n}"
  }
}
