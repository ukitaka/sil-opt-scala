package me.waft.swift.lang.`type`

sealed trait Throwing

case object Throws extends Throwing

case object Rethrows extends Throwing

object Throwing {
  def apply(name: String) = name match {
    case "throws" => Throws
    case "rethrows" => Rethrows
    case _ => Throws
  }
}
