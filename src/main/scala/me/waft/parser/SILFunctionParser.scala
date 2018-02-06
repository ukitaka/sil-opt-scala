package me.waft.parser

import fastparse.noApi._
import White._
import me.waft.parser.IdentifierParser.SIL
import me.waft.sil.SILFunction

object SILFunctionParser {
  // sil-function ::= 'sil' sil-linkage? sil-function-name ':' sil-type '{' sil-basic-block+ '}'
  def silFunction: P[SILFunction] = ???

  // sil-function-name ::= '@' [A-Za-z_0-9]+
  def silFunctionName: P[String] = ("@" ~ SIL.identifier).!
}
