package me.waft.sil.parser

import me.waft.sil.lang.SILType
import org.scalatest._

class SILTypeParserSpec extends FlatSpec with Matchers with SILTypeParser {
  "sil type" should "be parsed well" in {
    val sil = "$Dog Cat"
    silType.parse(sil).get.value shouldBe(SILType("Dog"))


  }
}
