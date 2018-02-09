package me.waft.parser.instruction

import fastparse.noApi._
import me.waft.parser.WhiteSpaceApi._
import me.waft.parser._
import me.waft.parser.SwiftTypeParser._
import me.waft.parser.SILTypeParser._
import me.waft.parser.SILOperandParser._
import me.waft.sil.instruction.{BuiltIn, SILSubstitution}

trait FunctionApplicationParser {
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
