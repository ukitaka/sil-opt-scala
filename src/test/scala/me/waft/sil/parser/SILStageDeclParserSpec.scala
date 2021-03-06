package me.waft.sil.parser

import me.waft.sil.lang.decl.SILStageDecl
import org.scalatest._

class SILStageDeclParserSpec extends FlatSpec with Matchers with SILStageDeclParser {
  "SILStageDecl parser" should "work well with sil_stage canonical" in {
    val sil = "sil_stage canonical"
    val result = silStageDecl.parse(sil).get.value
    result should be(SILStageDecl.Canonical)
  }

  "SILStageDecl parser" should "work well with sil_stage raw" in {
    val sil = "sil_stage raw"
    val result = silStageDecl.parse(sil).get.value
    result should be(SILStageDecl.Raw)
  }
}
