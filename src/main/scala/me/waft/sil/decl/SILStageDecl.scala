package me.waft.sil.decl

sealed abstract class SILStageDecl(val name: String) extends Decl {
  def isRaw: Boolean = (this == SILStageDecl.Raw)
  def isCanonical: Boolean = (this == SILStageDecl.Canonical)
}

object SILStageDecl {
  case object Raw extends SILStageDecl("raw")
  case object Canonical extends SILStageDecl("canonical")

  def apply(name: String): SILStageDecl = name match {
    case Canonical.name => Canonical
    case Raw.name => Raw
    case _ => Raw
  }
}
