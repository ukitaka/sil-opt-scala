package me.waft

import fastparse.WhitespaceApi

package object parser {
  val White = WhitespaceApi.Wrapper {
    import fastparse.all._
    val newline = P( "\n" | "\r\n" | "\r" | "\f")
    val whitespace = P( " " | "\t" | newline)
    NoTrace(whitespace.rep)
  }
}
