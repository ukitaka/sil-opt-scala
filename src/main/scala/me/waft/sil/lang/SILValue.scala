package me.waft.sil.lang

case class SILValue(name: String)

object SILValue {
  def undef = SILValue("undef")
  def number(i: Int) = SILValue("%" + i)
}
