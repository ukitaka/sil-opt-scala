package me.waft

import me.waft.parser.CommentParser._

package object parser {
  import fastparse.all._

  val newline = P( "\n" | "\r\n" | "\r" | "\f")

  val whitespace = P( " " | "\t" | newline)

  val whitespaces = (whitespace | comment).rep

  val trailingComma: P0 = P( ("," ~ whitespaces ~ newline).? )

  private val WL0 = NoTrace(whitespaces)

  class WhitespaceApi2[+T](p0: P[T], WL: P0) extends fastparse.WhitespaceApi[T](p0, WL) {
    def repTC[R](min: Int = 0, max: Int = Int.MaxValue, exactly: Int = -1)
                (implicit ev: fastparse.core.Implicits.Repeater[T, R]): P[R] =
      rep[R](min, ",", max, exactly) ~ trailingComma
  }

  class WhitespaceApiForSeq[+T](p0: P[Seq[T]], WL: P0) extends fastparse.WhitespaceApi[Seq[T]](p0, WL) {
    def ?? = ?.map(_.getOrElse(Seq.empty))
  }

  class Wrapper(WL: P0){
    implicit def parserApi2[T, V](p0: T)(implicit c: T => P[V]): WhitespaceApi2[V] =
      new WhitespaceApi2[V](p0, WL)

    implicit def parserForSeq[T](p0: P[Seq[T]]): WhitespaceApiForSeq[T] =
      new WhitespaceApiForSeq[T](p0, WL)
  }

  val WhiteSpaceApi = new Wrapper(WL0)
}

