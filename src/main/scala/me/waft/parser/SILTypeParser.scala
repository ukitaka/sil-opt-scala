package me.waft.parser

import fastparse.all._
import me.waft.sil.SILType
import me.waft.parser.IdentifierParser._

object SILTypeParser {
  def silType: P[SILType] = ("$" ~ "*".? ~ SIL.identifier).!.map(SILType.apply)
}
