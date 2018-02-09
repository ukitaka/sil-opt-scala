package me.waft.sil.parser

import fastparse.all._

object IdentifierParser {
  object SIL {
    def identifier: P[String] =
      CharIn( 'A' to 'Z', 'a' to 'z', '0' to '9', Seq('_') ).rep(1).!
  }

  object Swift {
    def identifier: P[String] = (identifierHead ~ identifierCharacters).!

    private[this] def identifierHead: P[String] =
      CharIn( 'A' to 'Z', 'a' to 'z', Seq('_') ).!

    private[this] def identifierCharacter: P[String] =
      ( CharIn('0' to '9') | identifierHead ).!

    private [this] def identifierCharacters: P[String] =
      identifierCharacter.rep(0).!
  }
}
