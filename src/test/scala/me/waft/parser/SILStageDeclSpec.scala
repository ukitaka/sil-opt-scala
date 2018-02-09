package me.waft.parser

import me.waft.lang.decl.SILStageDecl
import org.scalatest._

class SILStageDeclSpec extends FlatSpec with Matchers {
  "isRaw" should "work well" in {
    SILStageDecl.Raw.isRaw should be (true)
    SILStageDecl.Canonical.isRaw should be (false)
  }

  "isCanonical" should "work well" in {
    SILStageDecl.Raw.isCanonical should be (false)
    SILStageDecl.Canonical.isCanonical should be (true)
  }
}