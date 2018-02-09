package me.waft.parser.instruction

import fastparse.noApi._
import me.waft.parser.WhiteSpaceApi._
import me.waft.parser.SwiftTypeParser._
import me.waft.sil.instruction.{BuiltIn, SILSubstitution}

trait FunctionApplicationParser {
  def silSubstitutionList: P[Seq[SILSubstitution]] =
    "<" ~ silSubstitution.repTC(1) ~ ">"
  def silSubstitution: P[SILSubstitution] =
    (nominalType ~ "=" ~ nominalType).map(SILSubstitution.tupled)
}
