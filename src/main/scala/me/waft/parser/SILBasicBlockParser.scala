package me.waft.parser

import fastparse.all._
import me.waft.parser.IdentifierParser.SIL
import me.waft.parser.SILValueParser._
import me.waft.parser.SILTypeParser._
import me.waft.sil.{SILArgument, SILLabel}


object SILBasicBlockParser {
  def basicBlock: P[Unit] = ???

  def silLabel: P[SILLabel] =
    (SIL.identifier ~ silLabelArguments ~ ":")
      .map(SILLabel.tupled)

  private[this] def silLabelArguments: P[Seq[SILArgument]] =
    ( "(" ~ silArgument ~ ( "," ~ silArgument ).rep(0) ~ ")" ).?
      .map(_.map(args => args._1 +: args._2))
      .map(_.getOrElse(Seq.empty))

  private[this] def silArgument: P[SILArgument] =
    (silValueName ~ ":" ~ silType)
      .map(SILArgument.tupled)
}
