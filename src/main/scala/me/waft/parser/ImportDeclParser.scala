package me.waft.parser

import fastparse.all._
import me.waft.parser.IdentifierParser.Swift
import me.waft.sil.decl.ImportDecl

object ImportDeclParser {
  def importDecl: P[ImportDecl] =
    P("import " ~ moduleIdentifier).map(ImportDecl.apply _)

  def moduleIdentifier: P[String] = Swift.identifier
}
