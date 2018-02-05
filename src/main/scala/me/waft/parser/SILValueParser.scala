package me.waft.parser

import fastparse.all._
import me.waft.IdentifierParser.SIL
import me.waft.sil.SILValue

object SILValueParser {
  def silValue: P[SILValue] = silValueName.map(SILValue.apply _) | undef.map(_ => SILValue.undef)

  private[this] def silValueName: P[String] = "%" ~ SIL.identifier

  private[this] def undef: P[String] = "undef".!
}
