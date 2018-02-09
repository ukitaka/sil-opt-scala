package me.waft

import fastparse.WhitespaceApi
import me.waft.parser.CommentParser._

package object parser {
  import fastparse.all._

  val newline = P( "\n" | "\r\n" | "\r" | "\f")

  val whitespace = P( " " | "\t" | newline)

  val whitespaces = (whitespace | comment).rep

  val White = WhitespaceApi.Wrapper {
    import fastparse.all._
    NoTrace(whitespaces)
  }
}
