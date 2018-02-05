package me.waft.parser

import fastparse.all._
import me.waft.IdentifierParser
import me.waft.sil.decl.ImportDecl

object ImportDeclParser {
  def importDecl: P[ImportDecl] =
    P("import " ~ moduleIdentifier).map(ImportDecl.apply _)

  def moduleIdentifier: P[String] = IdentifierParser.identifier
}
