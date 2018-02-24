package me.waft.swift.lang.`type`

import me.waft.swift.lang.`type`.GenericParameter.Requirement

case class GenericParameter(typeParameters: Seq[SwiftType], requirements: Seq[Requirement])

object GenericParameter {
  sealed trait Requirement
  case class ConformanceRequirement(target: SwiftType,
                                    conformanceTo: SwiftType)
      extends Requirement

  case class SameTypeRequirement(type1: SwiftType, type2: SwiftType)
      extends Requirement
}
