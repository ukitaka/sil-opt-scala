package me.waft.parser

import fastparse.noApi._
import White._
import SILValueParser._
import me.waft.sil.{SILArgument, SILValue}

object SILInstructionParser {
  def silInstructionResult: P[Seq[SILValue]] =
    ("(" ~ silValueName ~ silInstructionResultNames ~ ")")
      .map(names => names._1 +: names._2)
      .map(_.map(SILValue.apply))

  private[this] def silInstructionResultNames: P[Seq[String]] = {
    ("," ~ silValueName).rep(0).?.map(_.getOrElse(Seq.empty))
  }

  private[this] def silInstructionSourceInfo: P[String] = ???

  def silInstruction: P[String] = ???

  //FIXME
  def silInstructionDef: P[String] = ((silInstructionResult ~ "=").? ~ silInstruction ~ silInstructionSourceInfo).!
}
