import me.waft.parser.ParseImportDecl
import me.waft.sil.decl.ImportDecl
import org.scalatest._

class ParseImportDeclSpec extends FlatSpec with Matchers {
  "parseImportDecl" should "work well" in {
    val sil = "import SwiftShims"
    val result = ParseImportDecl.importDecl.parse(sil).get.value
    result should be (ImportDecl("SwiftShims"))
  }
}