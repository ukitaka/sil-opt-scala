package me.waft.swift.lang.`type`

sealed trait SwiftType

/**
  * type → array-type
  * type → dictionary-type
  * type → function-type
  * type → tuple-type
  * type → optional-type
  * type → implicitly-unwrapped-optional-type
  * type → protocol-composition-type
  * type → metatype-type
  * type → Any
  * type → Self
  * type → (type)
  */
case class ArrayType(elementType: SwiftType) extends SwiftType

case class DictionaryType(keyType: SwiftType, valueType: SwiftType) extends SwiftType

case class TupleType(types: Seq[SwiftType]) extends SwiftType

case class OptionalType(wrappedType: SwiftType) extends SwiftType

case class ImplicitlyUnwrappedOptionalType(wrappedType: SwiftType) extends SwiftType

case class ProtocolCompositionType(types: Seq[SwiftType]) extends SwiftType

case class MetatypeType(instanceType: SwiftType) extends SwiftType

case object AnyType extends SwiftType

case object SelfType extends SwiftType

case class ParenType(`type`: SwiftType) extends SwiftType

case class FunctionType(attributes: Seq[Attribute], argType: SwiftType,
                        throwing: Option[Throwing], valueType: SwiftType) extends SwiftType

case class FunctionTypeArgument(attributes: Seq[Attribute], `type`: SwiftType) extends SwiftType

/**
  * type → type-identifier
  */
case class NominalType(name: String) extends SwiftType
