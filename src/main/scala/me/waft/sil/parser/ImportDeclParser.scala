package me.waft.sil.parser

import fastparse.noApi._
import me.waft.sil.parser.IdentifierParser.Swift
import me.waft.sil.parser.WhiteSpaceApi._
import me.waft.sil.lang.decl.ImportDecl

object ImportDeclParser {
  def importDecl: P[ImportDecl] =
    P("import" ~ moduleIdentifier).map(ImportDecl.apply _)

  def moduleIdentifier: P[String] = Swift.identifier
}
