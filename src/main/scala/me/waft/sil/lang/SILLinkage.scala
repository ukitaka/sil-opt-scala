package me.waft.sil.lang

sealed abstract class SILLinkage(val name: String)

case object Public extends SILLinkage("public")
case object Hidden extends SILLinkage("hidden")
case object Shared extends SILLinkage("shared")
case object Private extends SILLinkage("private")
case object PublicExternal extends SILLinkage("public_external")
case object HiddenExternal extends SILLinkage("hidden_external")

object SILLinkage {
  def apply(name: String): SILLinkage = name match {
    case Public.name => Public
    case Hidden.name => Hidden
    case Shared.name => Shared
    case Private.name => Private
    case PublicExternal.name => PublicExternal
    case HiddenExternal.name => HiddenExternal
  }
}
