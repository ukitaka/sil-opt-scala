package me.waft.sil.lang.decl

sealed trait TopDecl extends SILDecl

object TopDecl {
  case class Value(valueDecl: ValueDecl)
  case class Module(moduleDecl: ModuleDecl)
}
