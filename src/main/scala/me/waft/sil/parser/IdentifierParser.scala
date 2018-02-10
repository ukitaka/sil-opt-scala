package me.waft.sil.parser

import me.waft.core.parser.Parser
import fastparse.noApi._

trait IdentifierParser extends Parser {
  import WhiteSpaceApi._

  def identifier: P[String] =
    CharIn( 'A' to 'Z', 'a' to 'z', '0' to '9', Seq('_') ).rep(1).!
}
