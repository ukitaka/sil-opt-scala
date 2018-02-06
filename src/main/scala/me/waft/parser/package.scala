package me.waft

import fastparse.WhitespaceApi

package object parser {
  val White = WhitespaceApi.Wrapper {
    import fastparse.all._
    NoTrace(" ".rep)
  }
}
