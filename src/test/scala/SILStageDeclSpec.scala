import me.waft.sil.decl.SILStageDecl

import org.scalatest._

class SILStageDeclSpec extends FlatSpec with Matchers {
  "isRaw" should "works well" in {
    SILStageDecl.Raw.isRaw should be (true)
    SILStageDecl.Canonical.isRaw should be (false)
  }

  "isCanonical" should "works well" in {
    SILStageDecl.Raw.isCanonical should be (false)
    SILStageDecl.Canonical.isCanonical should be (true)
  }
}