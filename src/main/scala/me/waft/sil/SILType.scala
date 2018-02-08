package me.waft.sil

import me.waft.swift.`type`.{NominalType, SwiftType}

case class SILType(swiftType: SwiftType)

object SILType {
  def apply(name: String): SILType = SILType(NominalType(name))
}