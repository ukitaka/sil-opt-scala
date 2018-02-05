package me.waft.sil.decl

sealed trait TopDecl extends Decl

object TopDecl {
  case class Value(valueDecl: ValueDecl)
  case class Module(moduleDecl: ModuleDecl)
}