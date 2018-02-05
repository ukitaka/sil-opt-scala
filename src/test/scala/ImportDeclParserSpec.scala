import me.waft.parser.ParseImportDecl
import me.waft.sil.decl.ImportDecl
import org.scalatest._

class ImportDeclParserSpec extends FlatSpec with Matchers {
  "ImportDecl parser" should "work well" in {
    val sil = "import SwiftShims"
    val result = ParseImportDecl.importDecl.parse(sil).get.value
    result should be (ImportDecl("SwiftShims"))
  }
}