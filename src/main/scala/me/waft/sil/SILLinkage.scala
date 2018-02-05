package me.waft.sil

sealed abstract class SILLinkage(val name: String)

object SILLinkage {
  case object Public extends SILLinkage("public")
  case object Hidden extends SILLinkage("hidden")
  case object Shared extends SILLinkage("shared")
  case object Private extends SILLinkage("private")
  case object PublicExternal extends SILLinkage("public_external")
  case object HiddenExternal extends SILLinkage("hidden_external")
}
