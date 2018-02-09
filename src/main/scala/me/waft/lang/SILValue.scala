package me.waft.lang

case class SILValue(name: String)

object SILValue {
  def undef = SILValue("undef")
}
