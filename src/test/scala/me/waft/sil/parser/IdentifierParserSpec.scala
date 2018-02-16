package me.waft.sil.parser

import me.waft.swift.parser.SwiftIdentifierParser
import org.scalatest._

class IdentifierParserSpec extends FlatSpec with Matchers with IdentifierParser with SwiftIdentifierParser {
  "SIL Identifier parser" should "work well" in {
    val id = "abc1234 "
    val result = identifier.parse(id).get.value
    result should be("abc1234")
  }

  "Swift Identifier parser" should "work well" in {
    val identifier = "abc1234 //"
    val result = swiftIdentifier.parse(identifier).get.value
    result should be("abc1234")
  }
}
