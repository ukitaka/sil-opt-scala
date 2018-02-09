package me.waft.parser

import me.waft.sil.lang.decl.ImportDecl
import org.scalatest._

class ImportDeclParserSpec extends FlatSpec with Matchers {
  "ImportDecl parser" should "work well" in {
    val sil = "import SwiftShims"
    val result = ImportDeclParser.importDecl.parse(sil).get.value
    result should be (ImportDecl("SwiftShims"))
  }
}