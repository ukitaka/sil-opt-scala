package me.waft.sil.parser

import fastparse.all._
import me.waft.core.parser.Parser

trait IdentifierParser extends Parser {
  def identifier: P[String] =
    CharIn('A' to 'Z', 'a' to 'z', '0' to '9', Seq('_')).rep(1).!
}
