package me.waft.sil.parser

import fastparse.noApi._
import me.waft.sil.lang.SILValue

trait SILValueParser extends IdentifierParser {

  import WhiteSpaceApi._

  def silValue: P[SILValue] = silValueName.map(SILValue.apply _) | undef.const(SILValue.undef)

  def silValueName: P[String] = ("%" ~~ identifier).!

  private[this] def undef: P[String] = "undef".!
}
