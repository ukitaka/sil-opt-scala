package me.waft.parser

import me.waft.parser.IdentifierParser.Swift
import me.waft.sil.decl.ImportDecl
import fastparse.noApi._
import WhiteSpaceApi._

object ImportDeclParser {
  def importDecl: P[ImportDecl] =
    P("import" ~ moduleIdentifier).map(ImportDecl.apply _)

  def moduleIdentifier: P[String] = Swift.identifier
}
