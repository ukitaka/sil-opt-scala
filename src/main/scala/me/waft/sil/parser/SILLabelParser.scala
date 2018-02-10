package me.waft.sil.parser

import fastparse.noApi._
import me.waft.sil.lang.{SILArgument, SILLabel}

trait SILLabelParser extends IdentifierParser with SILValueParser with SILTypeParser {
  import WhiteSpaceApi._

  def silLabel: P[SILLabel] =
    (identifier ~ silLabelArguments ~ ":")
      .map(SILLabel.tupled)

  private[this] def silLabelArguments: P[Seq[SILArgument]] =
    ( "(" ~ silArgument.repTC(1) ~ ")" ).??

  private[this] def silArgument: P[SILArgument] =
    (silValueName ~ ":" ~ silType)
      .map(SILArgument.tupled)
}
