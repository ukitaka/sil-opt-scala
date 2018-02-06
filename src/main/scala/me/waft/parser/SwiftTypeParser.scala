package me.waft.parser

import fastparse.noApi._
import me.waft.parser.White._
import me.waft.parser.IdentifierParser.Swift
import me.waft.swift.`type`.{Attribute, FunctionType, Throwing, Type}

object SwiftTypeParser {
  def `type`: P[Type] = functionType

  def functionType: P[FunctionType] =
    (attributes.?.map(_.getOrElse(Seq.empty)) ~
      functionTypeArgumentClause ~
      throwing.? ~
      "->"
      ~ `type`).map(FunctionType.tupled)

  def functionTypeArgumentClause: P[Type] = ???

  private[this] def throwing: P[Throwing] = P("throws" | "rethrows").!.map(Throwing.apply _)

  private[this] def attributes: P[Seq[Attribute]] = attribute.rep(1)

  private[this] def attribute: P[Attribute] =
    ("@" ~ attributeName ~ attributeArgumentClause).map(Attribute.tupled)

  private[this] def attributeName: P[String] = Swift.identifier

  private[this] def attributeArgumentClause: P[Seq[String]] = "(" ~ balancedTokens ~ ")"

  private[this] def balancedTokens: P[Seq[String]] = balancedToken.rep(1)

  private[this] def balancedToken: P[String] = Swift.identifier //TODO あとで実装

}
