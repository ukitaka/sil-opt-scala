package me.waft.swift.parser

import fastparse.noApi._
import me.waft.swift.lang.`type`._

trait SwiftTypeParser extends SwiftIdentifierParser {
  import WhiteSpaceApi._

  def swiftType: P[SwiftType] = functionType | nominalType

  def nominalType: P[NominalType] =
    swiftIdentifier.rep(1, ".").!.map(NominalType)

  protected def functionType: P[FunctionType] =
    (attributes.?.map(_.getOrElse(Seq.empty)) ~
      functionTypeArgumentClause ~
      throwing.? ~
      "->"
      ~ nominalType).map(FunctionType.tupled)

  protected def functionTypeArgumentClause: P[TupleType] =
    functionTypeArgumentList.parened.map(TupleType)

  private[this] def functionTypeArgumentList: P[Seq[FunctionTypeArgument]] =
    functionTypeArgument.repTC(1)

  protected def functionTypeArgument: P[FunctionTypeArgument] =
    (attributes.?? ~ nominalType) //TODO: Suppor only nominal type for now
      .map(FunctionTypeArgument.tupled)

  private[this] def throwing: P[Throwing] = P("throws" | "rethrows").!.map(Throwing.apply _)

  // attributes

  protected def attributes: P[Seq[Attribute]] = attribute.rep(1)

  protected def attribute: P[Attribute] =
    ("@" ~ attributeName ~ attributeArgumentClause).map(Attribute.tupled)

  private[this] def attributeName: P[String] = swiftIdentifier

  private[this] def attributeArgumentClause: P[Seq[String]] = balancedTokens.parened

  private[this] def balancedTokens: P[Seq[String]] = balancedToken.rep(1)

  private[this] def balancedToken: P[String] = swiftIdentifier //TODO

}
