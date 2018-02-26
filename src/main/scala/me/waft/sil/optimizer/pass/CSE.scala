package me.waft.sil.optimizer.pass

import me.waft.sil.lang._
import me.waft.sil.optimizer.analysis.SILFunctionAnalysis
import me.waft.sil.optimizer.rewrite.{SILFunctionValueRenamer, SILValueReplacer}
import me.waft.sil.optimizer.util.Implicits._

case class CSE(function: SILFunction) {
  type Available = Map[SILInstruction, SILValue]
  type Replace = Map[SILValue, SILValue]

  case class EliminationInfo(available: Available, replace: Replace) {
    def addToReplace(instructionDef: SILInstructionDef) =
      EliminationInfo(available,
                      replace ++ instructionDef.values
                        .map(_ -> available(instructionDef.instruction))
                        .toMap)

    def makeAvailable(instructionDef: SILInstructionDef): EliminationInfo =
      EliminationInfo(
        available ++ instructionDef.values
          .map(v => instructionDef.instruction -> v)
          .toMap,
        replace
      )
  }

  object EliminationInfo {
    def empty: EliminationInfo = EliminationInfo(Map(), Map())
  }

  lazy val analysis = SILFunctionAnalysis(function)

  def eliminateCommonSubexpression: SILFunction = {
    val info = function.basicBlocks.foldLeft(EliminationInfo.empty)(eliminate)
    val optimized = function
      .filterInstructionDef(i => !i.values.forall(info.replace.contains))
      .mapBB(bb =>
        SILValueReplacer.replaceValuesInBasicBlock(bb)(v =>
          info.replace.getOrElse(v, v)))
    SILFunctionValueRenamer.renameValues(optimized)
  }

  def eliminate(info: EliminationInfo, bb: SILBasicBlock): EliminationInfo =
    bb.instructionDefs.foldLeft(info)(eliminate)

  def eliminate(info: EliminationInfo,
                instructionDef: SILInstructionDef): EliminationInfo =
    if (canHandle(instructionDef)) {
      if (info.available.contains(instructionDef.instruction)) {
        info.addToReplace(instructionDef)
      } else {
        info.makeAvailable(instructionDef)
      }
    } else {
      info
    }

  def canHandle(instructionDef: SILInstructionDef): Boolean =
    instructionDef.instruction match {
      //    case ClassMethod => true
      //    case SuperMethod => true
      //    case FunctionRef => true
      //    case GlobalAddr => true
      case IntegerLiteral(_, _) => true
      //    case FloatLiteral => true
      //    case StringLiteral => true
      case Struct(_, _)        => true
      case StructExtract(_, _) => true
      //    case StructElementAddr => true
      case Tuple(_)           => true
      case TupleExtract(_, _) => true
      //    case TupleElementAddr => true
      //    case Metatype => true
      //    case ValueMetatype => true
      //    case ObjCProtocol => true
      //    case RefElementAddr => true
      //    case RefTailAddr => true
      case ProjectBox(_) => true
      //    case IndexRawPointer => true
      //    case IndexAddr => true
      //    case PointerToAddress => true
      //    case AddressToPointer => true
      //    case CondFail => true
      //    case Enum => true
      //    case UncheckedEnumData => true
      //    case UncheckedTrivialBitCast => true
      //    case UncheckedBitwiseCast => true
      //    case RefToRawPointer => true
      //    case RawPointerToRef => true
      //    case RefToUnowned => true
      //    case UnownedToRef => true
      //    case RefToUnmanaged => true
      //    case UnmanagedToRef => true
      //    case Upcast => true
      //    case ThickToObjCMetatype => true
      //    case ObjCToThickMetatype => true
      //    case UncheckedRefCast => true
      //    case UncheckedAddrCast => true
      //    case ObjCMetatypeToObject => true
      //    case ObjCExistentialMetatypeToObject => true
      //    case SelectEnum => true
      //    case SelectValue => true
      //    case RefToBridgeObject => true
      //    case BridgeObjectToRef => true
      //    case BridgeObjectToWord => true
      //    case ThinFunctionToPointer => true
      //    case PointerToThinFunction => true
      //    case MarkDependence => true
      case OpenExistentialRef(_, _) => true
      case _ => false
    }
}

object CSE extends SILFunctionTransform {
  def run(function: SILFunction): SILFunction =
    CSE(function).eliminateCommonSubexpression
}
