package me.waft.parser

import fastparse.all._
import me.waft.sil.decl.ImportDecl

object ParseImportDecl {
  def importDecl: P[ImportDecl] =
    P("import " ~ moduleIdentifier).map(ImportDecl.apply _)

  def moduleIdentifier: P[String] =
    (upper | lower | digit).rep(1).!
}
