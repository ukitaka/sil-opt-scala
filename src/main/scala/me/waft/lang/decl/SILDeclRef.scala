package me.waft.lang.decl

case class SILDeclRef(declRef: String, subref: Option[SILDeclSubref])

case class SILDeclSubref(subrefPart: Option[SILDeclSubrefPart],
                           uncurryLevel: Option[SILDeclUncurryLevel],
                           declLang: Option[SILDeclLang])

sealed trait SILDeclSubrefPart

object SILDeclSubrefPart {
  case object Getter extends SILDeclSubrefPart
  case object Setter extends SILDeclSubrefPart
  case object Allocator extends SILDeclSubrefPart
  case object Initializer extends SILDeclSubrefPart
  case object Enumelt extends SILDeclSubrefPart
  case object Destoryer extends SILDeclSubrefPart
  case object Deallocator extends SILDeclSubrefPart
  case object Globalaccessor extends SILDeclSubrefPart
  case object Ivardestroyer extends SILDeclSubrefPart
  case object Ivarinitializer extends SILDeclSubrefPart
  case class Defaultarg(id: Int) extends SILDeclSubrefPart
}

case class SILDeclUncurryLevel(level: Int)

sealed trait SILDeclLang

object SILDeclLang {
  case object Foreign extends SILDeclLang
}
