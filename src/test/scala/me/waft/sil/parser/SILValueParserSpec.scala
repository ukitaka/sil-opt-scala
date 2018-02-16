package me.waft.sil.parser

import me.waft.sil.lang.SILValue
import org.scalatest._

class SILValueParserSpec extends FlatSpec with Matchers with SILValueParser {
  "sil value" should "be parsed well" in {
    val sil = "%1"
    silValue.parse(sil).get.value should be(SILValue("%1"))
  }
}
