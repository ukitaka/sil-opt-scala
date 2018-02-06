package me.waft.sil

case class SILValue(name: String)

object SILValue {
  def undef = SILValue("undef")
}
