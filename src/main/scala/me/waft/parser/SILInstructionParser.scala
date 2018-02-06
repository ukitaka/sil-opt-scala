package me.waft.parser

import fastparse.noApi._
import White._
import SILValueParser._
import me.waft.sil.{SILArgument, SILInstruction, SILInstructionDef, SILValue}

object SILInstructionParser {
  def silInstructionResult: P[Seq[SILValue]] =
    ("(" ~ silValueName ~ silInstructionResultNames ~ ")")
      .map(names => names._1 +: names._2)
      .map(_.map(SILValue.apply))

  private[this] def silInstructionResultNames: P[Seq[String]] =
    ("," ~ silValueName).rep(0).?.map(_.getOrElse(Seq.empty))

  private[this] def silInstructionSourceInfo: P[Option[String]] = ???

  def silInstruction: P[SILInstruction] = ???

  def silInstructionDef: P[SILInstructionDef] = (
    (silInstructionResult ~ "=").?.map(_.getOrElse(Seq.empty)) ~
      silInstruction ~
      silInstructionSourceInfo)
    .map(p => SILInstructionDef(p._1, p._2))
}
