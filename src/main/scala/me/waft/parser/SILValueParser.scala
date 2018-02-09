package me.waft.parser

import fastparse.noApi._
import me.waft.parser.IdentifierParser.SIL
import me.waft.parser.WhiteSpaceApi._
import me.waft.sil.lang.SILValue

object SILValueParser {
  def silValue: P[SILValue] = silValueName.map(SILValue.apply _) | undef.map(_ => SILValue.undef)

  def silValueName: P[String] = ("%" ~ SIL.identifier).!

  private[this] def undef: P[String] = "undef".!
}
