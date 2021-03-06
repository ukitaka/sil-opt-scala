package me.waft.sil.lang

import me.waft.swift.lang.`type`.{NominalType, SwiftType}

case class SILType(swiftType: SwiftType)

object SILType {
  def apply(name: String): SILType = SILType(NominalType(name))
}