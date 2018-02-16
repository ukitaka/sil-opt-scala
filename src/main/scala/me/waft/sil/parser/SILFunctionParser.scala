package me.waft.sil.parser

import fastparse.noApi._
import me.waft.sil.lang.SILFunction

trait SILFunctionParser extends SILBasicBlockParser with SILLinkageParser {

  import WhiteSpaceApi._

  // sil-function ::= 'sil' sil-linkage? sil-function-name ':' sil-type '{' sil-basic-block+ '}'
  def silFunction: P[SILFunction] =
    ("sil" ~ silLinkage.? ~ silFunctionName ~ ":" ~ silType ~ "{" ~ basicBlock.rep(1) ~ "}")
      .map(SILFunction.tupled)

  // sil-function-name ::= '@' [A-Za-z_0-9]+
  def silFunctionName: P[String] = ("@" ~~ identifier).!
}
