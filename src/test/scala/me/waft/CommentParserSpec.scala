package me.waft

import me.waft.parser.CommentParser
import org.scalatest._

class CommentParserSpec extends FlatSpec with Matchers {
  "Comment parser" should "work well" in {
    val sil = "// here is comment\n"
    val result = CommentParser.comment.parse(sil).get.value
    result should be ()
  }
}
