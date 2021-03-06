package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.{Apply, BuiltIn, SILSubstitution, SILType}
import me.waft.sil.parser._

trait FunctionApplicationParser extends SILOperandParser with SILTypeParser {

  import WhiteSpaceApi._

  def silSubstitutionList: P[Seq[SILSubstitution]] =
    "<" ~ silSubstitution.repTC(1) ~ ">"

  def silSubstitution: P[SILSubstitution] =
    (nominalType | annotatedType).map(SILSubstitution)

  def builtin: P[BuiltIn] =
    ("builtin"
      ~ stringLiteral
      ~ silSubstitutionList.??
      ~ silOperand.repTC(1).parened
      ~ ":" ~ silType).map(BuiltIn.tupled)

  private def noThrow: P[Boolean] = P("nothrow").!.?.map(_.isDefined)

  def functionApply: P[Apply] =
    ("apply" ~ noThrow ~ silValue ~ silSubstitutionList.?? ~ silValue
      .repTC(1)
      .parened ~ ":" ~ silType).map(Apply.tupled)
}
