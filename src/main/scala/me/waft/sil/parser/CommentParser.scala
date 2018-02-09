package me.waft.sil.parser

import fastparse.all._

object CommentParser {
  val comment = P( "//" ~/ (!"\n" ~ AnyChar).rep ~/ ("\n" | End))
}
