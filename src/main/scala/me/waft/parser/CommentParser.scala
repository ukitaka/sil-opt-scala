package me.waft.parser

import fastparse.all._

object CommentParser {
  val comment = P( "//" ~/ (!"\n" ~ AnyChar).rep ~/ "\n")
}
