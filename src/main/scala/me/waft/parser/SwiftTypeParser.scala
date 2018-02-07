package me.waft.parser

import fastparse.noApi._
import me.waft.parser.White._
import me.waft.parser.IdentifierParser.Swift
import me.waft.swift.`type`._

object SwiftTypeParser {
  def `type`: P[SwiftType] = functionType | nominalType

  def nominalType: P[NominalType] = Swift.identifier.map(NominalType)

  def functionType: P[FunctionType] =
    (attributes.?.map(_.getOrElse(Seq.empty)) ~
      functionTypeArgumentClause ~
      throwing.? ~
      "->"
      ~ nominalType).map(FunctionType.tupled)

  def functionTypeArgumentClause: P[TupleType] =
    "(" ~ functionTypeArgumentList.map(TupleType) ~ ")"

  private[this] def functionTypeArgumentList: P[Seq[FunctionTypeArgument]] =
    functionTypeArgument.map(arg => Seq(arg)) |
      ( functionTypeArgument ~ "," ).rep(1)

  def functionTypeArgument: P[FunctionTypeArgument] =
    (attributes.?.map(_.getOrElse(Seq.empty)) ~ nominalType) //TODO: Suppor only nominal type for now
      .map(FunctionTypeArgument.tupled)

  private[this] def argumentLabel: P[String] = Swift.identifier

  private[this] def throwing: P[Throwing] = P("throws" | "rethrows").!.map(Throwing.apply _)

  // attributes

  def attributes: P[Seq[Attribute]] = attribute.rep(1)

  def attribute: P[Attribute] =
    ("@" ~ attributeName ~ attributeArgumentClause).map(Attribute.tupled)

  private[this] def attributeName: P[String] = Swift.identifier

  private[this] def attributeArgumentClause: P[Seq[String]] = "(" ~ balancedTokens ~ ")"

  private[this] def balancedTokens: P[Seq[String]] = balancedToken.rep(1)

  private[this] def balancedToken: P[String] = Swift.identifier //TODO

}
