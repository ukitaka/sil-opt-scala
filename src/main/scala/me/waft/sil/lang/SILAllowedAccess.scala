package me.waft.sil.lang

sealed abstract class SILAllowedAccess(val name: String)
case object ImmutableAccess extends SILAllowedAccess("immutable_access")
case object MutableAccess extends SILAllowedAccess("mutable_access")

object SILAllowedAccess {
  def apply(string: String): SILAllowedAccess = string match {
    case ImmutableAccess.name => ImmutableAccess
    case MutableAccess.name => MutableAccess
    case _ => ???
  }
}
