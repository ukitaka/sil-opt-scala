package me.waft.parser

import fastparse.all._
import me.waft.sil.SILArgument

object SILBasicBlockParser {
  def basicBlock: P[Unit] = ???

  // sil-label ::= sil-identifier ('(' sil-argument (',' sil-argument)* ')')? ':'
  def silLabel: P[SILArgument] = ???

  // sil-argument ::= sil-value-name ':' sil-type
  def silArgument: P[SILArgument] = ???
}
