package me.waft.swift.parser

import fastparse.all._
import me.waft.core.parser.Parser

trait SwiftIdentifierParser extends Parser {
  def swiftIdentifier: P[String] = (identifierHead ~ identifierCharacters).!

  private[this] def identifierHead: P[String] =
    CharIn('A' to 'Z', 'a' to 'z', Seq('_', 'τ')).!

  private[this] def identifierCharacter: P[String] =
    (CharIn('0' to '9').! | identifierHead.!)

  private[this] def identifierCharacters: P[String] =
    identifierCharacter.rep(0).!
}
