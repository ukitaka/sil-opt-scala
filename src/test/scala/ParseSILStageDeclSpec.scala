import me.waft.parser.ParseSILStageDecl
import me.waft.sil.decl.SILStageDecl
import org.scalatest._

class ParseSILStageDeclSpec extends FlatSpec with Matchers {
  "parseSILStageDecl" should "work well with sil_stage canonical" in {
    val sil = "sil_stage canonical"
    val result = ParseSILStageDecl.silStageDecl.parse(sil).get.value
    result should be (SILStageDecl.Canonical)
  }

  "parseSILStageDecl" should "work well with sil_stage raw" in {
    val sil = "sil_stage raw"
    val result = ParseSILStageDecl.silStageDecl.parse(sil).get.value
    result should be (SILStageDecl.Raw)
  }
}
