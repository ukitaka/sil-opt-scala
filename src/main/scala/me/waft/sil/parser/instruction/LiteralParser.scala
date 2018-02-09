package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.instruction.IntegerLiteral
import me.waft.sil.parser.SILTypeParser._
import me.waft.sil.parser.WhiteSpaceApi._

trait LiteralParser {
  def integerLiteral: P[IntegerLiteral] =
    ("integer_literal" ~ silType ~ "," ~ intLiteral).map(IntegerLiteral.tupled)

  private def intLiteral: P[Int] =
    ("-".? ~ CharIn('1' to '9') ~ CharIn('0' to '9').rep(0)).!.map(_.toInt) |
      "0".!.map(_.toInt)
}
