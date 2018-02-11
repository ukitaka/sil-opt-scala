package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.SILSubstitution
import me.waft.sil.lang.instruction.BuiltIn
import me.waft.sil.parser._

trait FunctionApplicationParser extends SILOperandParser with SILTypeParser {
  import WhiteSpaceApi._

  def silSubstitutionList: P[Seq[SILSubstitution]] =
    "<" ~ silSubstitution.repTC(1) ~ ">"
  def silSubstitution: P[SILSubstitution] =
    (nominalType ~ "=" ~ nominalType).map(SILSubstitution.tupled)

  def builtin: P[BuiltIn] =
    ("builtin"
      ~ stringLiteral
      ~ silSubstitutionList.??
      ~ silOperand.repTC(1).parened
      ~ ":" ~ silType ).map(BuiltIn.tupled)
}
