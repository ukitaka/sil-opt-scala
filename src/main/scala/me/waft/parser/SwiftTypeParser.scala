package me.waft.parser

import fastparse.noApi._
import me.waft.parser.White._
import me.waft.parser.IdentifierParser.Swift
import me.waft.swift.`type`._

object SwiftTypeParser {
  def `type`: P[Type] = nominalType | functionType

  def nominalType: P[NominalType] = Swift.identifier.map(NominalType)

  def functionType: P[FunctionType] =
    (attributes.?.map(_.getOrElse(Seq.empty)) ~
      functionTypeArgumentClause ~
      throwing.? ~
      "->"
//      ~ `type`).map(FunctionType.tupled)
        ~ nominalType).map(FunctionType.tupled)

  private[this] def functionTypeArgumentClause: P[Type] =
    ("("  ~ functionTypeArgumentList ~ ")").map(TupleType)

  private[this] def functionTypeArgumentList: P[Seq[FunctionTypeArgument]] =
    functionTypeArgument.map(arg => Seq(arg)) |
      ( functionTypeArgument ~ "," ).rep(1)

  private[this] def functionTypeArgument: P[FunctionTypeArgument] =
//    (attributes ~ `type`).map(FunctionTypeArgument.tupled)
      (attributes ~ nominalType).map(FunctionTypeArgument.tupled)

  private[this] def argumentLabel: P[String] = Swift.identifier

  private[this] def throwing: P[Throwing] = P("throws" | "rethrows").!.map(Throwing.apply _)

  // attributes

  private[this] def attributes: P[Seq[Attribute]] = attribute.rep(1)

  private[this] def attribute: P[Attribute] =
    ("@" ~ attributeName ~ attributeArgumentClause).map(Attribute.tupled)

  private[this] def attributeName: P[String] = Swift.identifier

  private[this] def attributeArgumentClause: P[Seq[String]] = "(" ~ balancedTokens ~ ")"

  private[this] def balancedTokens: P[Seq[String]] = balancedToken.rep(1)

  private[this] def balancedToken: P[String] = Swift.identifier //TODO あとで実装

}
