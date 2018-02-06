package me.waft.swift.`type`

sealed trait Type
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
case class ArrayType(elementType: Type) extends Type
case class DictionaryType(keyType: Type, valueType: Type) extends Type
case class TupleType(types: Seq[Type]) extends Type
case class OptionalType(wrappedType: Type) extends Type
case class ImplicitlyUnwrappedOptionalType(wrappedType: Type) extends Type
case class ProtocolCompositionType(types: Seq[Type]) extends Type
case class MetatypeType(instanceType: Type) extends Type
case object AnyType extends Type
case object SelfType extends Type
case class ParenType(`type`: Type) extends Type

case class FunctionType(attributes: Seq[Attribute], argType: Type,
                        throwing: Option[Throwing], valueType: Type) extends Type

case class FunctionTypeArgument(attributes: Seq[Attribute], `type`: Type) extends Type

/**
  * type → type-identifier
  */
case class NominalType(name: String) extends Type
