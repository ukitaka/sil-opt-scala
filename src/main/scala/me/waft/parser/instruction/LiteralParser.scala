package me.waft.parser.instruction

import fastparse.noApi._
import me.waft.parser.SILTypeParser._
import me.waft.parser.WhiteSpaceApi._
import me.waft.sil.lang.instruction.IntegerLiteral

trait LiteralParser {
  def integerLiteral: P[IntegerLiteral] =
    ("integer_literal" ~ silType ~ "," ~ intLiteral).map(IntegerLiteral.tupled)

  private def intLiteral: P[Int] =
    ("-".? ~ CharIn('1' to '9') ~ CharIn('0' to '9').rep(0)).!.map(_.toInt) |
      "0".!.map(_.toInt)
}
