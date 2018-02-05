package me.waft

import fastparse.all._

object Identifier {

  def identifier: P[String] = identifierHead ~ identifierCharacters

  private[this] def identifierHead = CharIn(
    'A' to 'Z',
    'a' to 'z',
    Seq('_', '\u00A8', '\u00AA', '\u00AD', '\u00AF'),
    '\u00B2' to '\u00B5', '\u00B7' to '\u00BA', '\u00BC' to '\u00BE',
    '\u00C0' to '\u00D6', '\u00D8' to '\u00F6', '\u00F8' to '\u00FF',
    '\u0100' to '\u02FF', '\u0370' to '\u167F', '\u1681' to '\u180D',
    '\u180F' to '\u1DBF', '\u1E00' to '\u1FFF', '\u200B' to '\u200D',
    '\u202A' to '\u202E', '\u203F' to '\u2040',
    Seq('\u2054'),
    '\u2060' to '\u206F', '\u2070' to '\u20CF', '\u2100' to '\u218F',
    '\u2460' to '\u24FF', '\u2776' to '\u2793', '\u2C00' to '\u2DFF',
    '\u2E80' to '\u2FFF', '\u3004' to '\u3007', '\u3021' to '\u302F',
    '\u3031' to '\u303F', '\u3040' to '\uD7FF', '\uF900' to '\uFD3D',
    '\uFD40' to '\uFDCF', '\uFDF0' to '\uFE1F', '\uFE30' to '\uFE44',
    '\uFE47' to '\uFFFD',
  )

  private[this] def identifierCharacter = CharIn(
    '0' to '9',
    '\u0300' to '\u036F', '\u1DC0' to '\u1DFF',
    '\u20D0' to '\u20FF', '\uFE20' to '\uFE2F'
  ) | identifierHead

  private [this] def identifierCharacters: P[String] = identifierCharacter ~ identifierCharacters
}
