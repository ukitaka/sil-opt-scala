package me.waft.parser

import fastparse.noApi._
import WhiteSpaceApi._
import me.waft.parser.IdentifierParser.SIL
import me.waft.sil.SILValue

object SILValueParser {
  def silValue: P[SILValue] = silValueName.map(SILValue.apply _) | undef.map(_ => SILValue.undef)

  def silValueName: P[String] = ("%" ~ SIL.identifier).!

  private[this] def undef: P[String] = "undef".!
}
