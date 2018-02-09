package me.waft.swift.parser

import fastparse.noApi._
import me.waft.sil.parser.IdentifierParser.Swift
import me.waft.sil.parser.WhiteSpaceApi._
import me.waft.swift.lang.`type`._

object SwiftTypeParser {
  def `type`: P[SwiftType] = functionType | nominalType

  def nominalType: P[NominalType] =
    Swift.identifier.rep(1, ".").!.map(NominalType)

  def functionType: P[FunctionType] =
    (attributes.?.map(_.getOrElse(Seq.empty)) ~
      functionTypeArgumentClause ~
      throwing.? ~
      "->"
      ~ nominalType).map(FunctionType.tupled)

  def functionTypeArgumentClause: P[TupleType] =
    functionTypeArgumentList.parened.map(TupleType)

  private[this] def functionTypeArgumentList: P[Seq[FunctionTypeArgument]] =
    functionTypeArgument.repTC(1)

  def functionTypeArgument: P[FunctionTypeArgument] =
    (attributes.?? ~ nominalType) //TODO: Suppor only nominal type for now
      .map(FunctionTypeArgument.tupled)

  private[this] def argumentLabel: P[String] = Swift.identifier

  private[this] def throwing: P[Throwing] = P("throws" | "rethrows").!.map(Throwing.apply _)

  // attributes

  def attributes: P[Seq[Attribute]] = attribute.rep(1)

  def attribute: P[Attribute] =
    ("@" ~ attributeName ~ attributeArgumentClause).map(Attribute.tupled)

  private[this] def attributeName: P[String] = Swift.identifier

  private[this] def attributeArgumentClause: P[Seq[String]] = balancedTokens.parened

  private[this] def balancedTokens: P[Seq[String]] = balancedToken.rep(1)

  private[this] def balancedToken: P[String] = Swift.identifier //TODO

}
