package me.waft.sil.lang

sealed abstract class SILAllowedAccess(name: String)
case object ImmutableAccess extends SILAllowedAccess("immutable_access")
case object MutableAccess extends SILAllowedAccess("mutable_access")
