package me.waft.sil.parser

import me.waft.core.parser.Parser
import fastparse.all._

trait IdentifierParser extends Parser {
  def identifier: P[String] =
    CharIn( 'A' to 'Z', 'a' to 'z', '0' to '9', Seq('_') ).rep(1).!
}
