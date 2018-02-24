package me.waft.swift.parser

import fastparse.noApi._
import me.waft.swift.lang.`type`.GenericParameter.{
  ConformanceRequirement,
  Requirement
}
import me.waft.swift.lang.`type`._

trait SwiftTypeParser extends SwiftIdentifierParser {

  import WhiteSpaceApi._

  def swiftType: P[SwiftType] =
    selfType | genericFunctionType | functionType |
      protocolCompositionType | annotatedType | nominalType | tupleType

  def nominalType: P[NominalType] =
    swiftIdentifier.rep(1, ".").!.map(NominalType)

  def selfType: P[SwiftType] = "Self".const(SelfType)

  // TODO: Support only `(NominalType ...)` for now.
  def tupleType: P[TupleType] =
    ("(" ~ nominalType.repTC(0) ~ ")")
      .map(TupleType.apply)

  // TODO: Support only case of A & B, cannot parse type in the form of A & B & C for now.
  protected def protocolCompositionType: P[ProtocolCompositionType] =
    (nominalType ~ "&" ~ nominalType).map {
      case (t1, t2) => ProtocolCompositionType(Seq(t1, t2))
    }

  protected def annotatedType: P[AnnotatedType] =
    (attributes ~ nominalType).map(AnnotatedType.tupled)

  protected def genericFunctionType: P[GenericFunctionType] =
    (attributes.?.map(_.getOrElse(Seq.empty))
      ~ genericParameterClause
      ~ functionTypeArgumentClause
      ~ throwing.?
      ~ "->"
      ~/ (nominalType | tupleType))
      .map(GenericFunctionType.tupled)

  protected def functionType: P[FunctionType] =
    (attributes.?.map(_.getOrElse(Seq.empty))
      ~ functionTypeArgumentClause
      ~ throwing.?
      ~ "->"
      ~/ (nominalType | tupleType))
      .map(FunctionType.tupled)

  protected def functionTypeArgumentClause: P[TupleType] =
    functionTypeArgumentList.parened.map(TupleType)

  private[this] def functionTypeArgumentList: P[Seq[FunctionTypeArgument]] =
    functionTypeArgument.repTC(0)

  protected def functionTypeArgument: P[FunctionTypeArgument] =
    (attributes.?? ~ nominalType) //TODO: Suppor only nominal / tuple type for now
      .map(FunctionTypeArgument.tupled)

  private[this] def throwing: P[Throwing] =
    P("throws" | "rethrows").!.map(Throwing.apply _)

  // attributes

  protected def attributes: P[Seq[Attribute]] = attribute.rep(1)

  protected def attribute: P[Attribute] =
    ("@" ~ attributeName ~ attributeArgumentClause.??).map(Attribute.tupled)

  private[this] def attributeName: P[String] = swiftIdentifier

  private[this] def attributeArgumentClause: P[Seq[String]] =
    balancedTokens.parened

  private[this] def balancedTokens: P[Seq[String]] = balancedToken.rep(1)

  private[this] def balancedToken: P[String] =
    ("witness_method" ~ ":" ~ swiftIdentifier).! | //TODO
      swiftIdentifier | stringLiteral

  // generics

  def genericParameterClause: P[GenericParameter] =
    ("<" ~ genericParameterList ~ genericWhereClause.?? ~ ">")
      .map(g => GenericParameter.apply(g._1, g._2)) // TODO: For some reason, cannot use `.tupled`.

  private[this] def genericParameterList: P[Seq[SwiftType]] = genericParameter.repTC(1)

  private[this] def genericParameter: P[SwiftType] = nominalType //TODO

  private[this] def genericWhereClause: P[Seq[Requirement]] = "where" ~ requirementList

  private[this] def requirementList: P[Seq[Requirement]] = requirement.repTC(1)

  private[this] def requirement: P[Requirement] = conformanceRequirement | sameTypeRequirement

  private[this] def conformanceRequirement: P[Requirement] =
    ((nominalType | selfType) ~ ":" ~ nominalType)
      .map(ConformanceRequirement.tupled)

  private[this] def sameTypeRequirement: P[Requirement] =
    (nominalType ~ "==" ~ nominalType).map(ConformanceRequirement.tupled)
}
