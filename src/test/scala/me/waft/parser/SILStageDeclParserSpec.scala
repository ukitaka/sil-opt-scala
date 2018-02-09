package me.waft.parser

import me.waft.sil.lang.decl.SILStageDecl
import org.scalatest._

class SILStageDeclParserSpec extends FlatSpec with Matchers {
  "SILStageDecl parser" should "work well with sil_stage canonical" in {
    val sil = "sil_stage canonical"
    val result = SILStageDeclParser.silStageDecl.parse(sil).get.value
    result should be (SILStageDecl.Canonical)
  }

  "SILStageDecl parser" should "work well with sil_stage raw" in {
    val sil = "sil_stage raw"
    val result = SILStageDeclParser.silStageDecl.parse(sil).get.value
    result should be (SILStageDecl.Raw)
  }
}
