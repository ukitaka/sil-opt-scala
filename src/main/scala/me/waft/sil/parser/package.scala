package me.waft.sil

import me.waft.sil.parser.CommentParser._

package object parser {
  import fastparse.all._

  def number: P[Int] = CharIn('0' to '9').rep(1).!.map(_.toInt)

  def stringLiteral: P[String] = ("\"" ~ (!"\"" ~ AnyChar).rep.! ~ "\"")

  val newline = P( "\n" | "\r\n" | "\r" | "\f")

  val whitespace = P( " " | "\t" | newline)

  val whitespaces = (whitespace | comment).rep

  val trailingComma: P0 = P( ("," ~ whitespaces ~ newline).? )

  private val WL0 = NoTrace(whitespaces)

  class WhitespaceApi0[+T](p0: P[T], WL: P0) extends fastparse.WhitespaceApi[T](p0, WL) {
    def repTC[R](min: Int = 0, max: Int = Int.MaxValue, exactly: Int = -1)
                (implicit ev: fastparse.core.Implicits.Repeater[T, R]): P[R] =
      rep[R](min, ",", max, exactly) ~ trailingComma

    def const[T](t: T): P[T] = map(_ => t)

    def parened: P[T] = "(" ~ p0 ~ ")"
  }

  class WhitespaceApiSeq[+T](p0: P[Seq[T]], WL: P0) extends fastparse.WhitespaceApi[Seq[T]](p0, WL) {
    def ?? = ?.map(_.getOrElse(Seq.empty))
  }

  class Wrapper(WL: P0){
    implicit def parserApi[T, V](p0: T)(implicit c: T => P[V]): WhitespaceApi0[V] =
      new WhitespaceApi0[V](p0, WL)
    implicit def parserForSeq[T](p0: P[Seq[T]]): WhitespaceApiSeq[T] =
      new WhitespaceApiSeq[T](p0, WL)
  }

  val WhiteSpaceApi = new Wrapper(WL0)
}

