package me.waft.sil.parser

import org.scalatest._

class SILLabelParserSpec extends FlatSpec with Matchers with SILLabelParser {
  "SIL label" should "be parsed well" in {
    val sil = "bb1(%3 : $Builtin.Int32, %4 : $Builtin.Int32):"
    val label = silLabel.parse(sil).get.value
    label.identifier shouldBe ("bb1")
  }
}
