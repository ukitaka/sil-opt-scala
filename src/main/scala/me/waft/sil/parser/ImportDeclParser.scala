package me.waft.sil.parser

import fastparse.noApi._
import me.waft.sil.lang.decl.ImportDecl

trait ImportDeclParser extends IdentifierParser {

  import WhiteSpaceApi._

  def importDecl: P[ImportDecl] =
    P("import" ~ moduleIdentifier).map(ImportDecl.apply _)

  def moduleIdentifier: P[String] = identifier
}
