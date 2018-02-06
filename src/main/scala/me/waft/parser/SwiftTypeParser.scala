package me.waft.parser

import fastparse.noApi._
import me.waft.parser.White._
import me.waft.swift.`type`.{Attribute, FunctionType, Throwing, Type}

object SwiftTypeParser {
  def `type`: P[Type] = functionType

  def functionType: P[FunctionType] =
    (attributes.?.map(_.getOrElse(Seq.empty)) ~
      functionTypeArgumentClause ~
      throwing.? ~
      "->"
      ~ `type`).map(FunctionType.tupled)

  def attributes: P[Seq[Attribute]] = ???

  def functionTypeArgumentClause: P[Type] = ???

  private[this] def throwing: P[Throwing] = P("throws" | "rethrows").!.map(Throwing.apply _)
}
