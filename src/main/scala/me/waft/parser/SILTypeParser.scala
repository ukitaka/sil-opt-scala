package me.waft.parser

import fastparse.noApi._
import me.waft.parser.White._
import me.waft.sil.SILType
import me.waft.swift.`type`.GenericParameter

object SILTypeParser {
  def silType: P[SILType] = ("$" ~ "*".? ~ IdentifierParser.SIL.identifier).!.map(SILType.apply)

  object Swift {
    import IdentifierParser.Swift._

    def typeName: P[String] = identifier

    def typeIdentifier: P[(String, Seq[GenericParameter])] =
      typeName ~ genericParameterList.?.map(_.getOrElse(Seq.empty))

    // Generics
    //TODO: Implement case that generic param has some conformances. e.g. `T: P`
    def genericParameter: P[GenericParameter] =
      typeName.map(GenericParameter.apply(_, None))

    def genericParameterList: P[Seq[GenericParameter]] = genericParameter.rep(1)

    def genericParameterClause: P[Seq[GenericParameter]] = "<" ~ genericParameterList ~ ">"

  }
}
