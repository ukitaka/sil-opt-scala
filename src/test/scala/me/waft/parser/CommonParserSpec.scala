package me.waft.parser

import org.scalatest._

class CommonParserSpec extends FlatSpec with Matchers {
  "String literal" should "be parsed well" in {
    val s = "\"hello, world!\""
    stringLiteral.parse(s).get.value should be ("hello, world!")
  }

}
