package me.waft.swift.parser

import fastparse.noApi._
import me.waft.core.parser.Parser

trait SwiftIdentifierParser extends Parser {
  import WhiteSpaceApi._

  def swiftIdentifier: P[String] = (identifierHead ~~ identifierCharacters).!

  private[this] def identifierHead: P[String] =
    CharIn( 'A' to 'Z', 'a' to 'z', Seq('_') ).!

  private[this] def identifierCharacter: P[String] =
    ( CharIn('0' to '9').! | identifierHead.! )

  private [this] def identifierCharacters: P[String] =
    identifierCharacter.rep(0).!
}
