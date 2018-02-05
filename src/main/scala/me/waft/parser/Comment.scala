package me.waft.parser

import fastparse.all._

object Comment {
  val comment = P( "//" ~/ (!"\n" ~ AnyChar).rep ~/ "\n")
}
