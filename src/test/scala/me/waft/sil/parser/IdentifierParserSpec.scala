package me.waft.sil.parser

import org.scalatest._

class IdentifierParserSpec extends FlatSpec with Matchers {
  "SIL Identifier parser" should "work well" in {
    val identifier = "abc1234 "
    val result = IdentifierParser.SIL.identifier.parse(identifier).get.value
    result should be ("abc1234")
  }

  "Swift Identifier parser" should "work well" in {
    val identifier = "abc1234 "
    val result = IdentifierParser.Swift.identifier.parse(identifier).get.value
    result should be ("abc1234")
  }
}
