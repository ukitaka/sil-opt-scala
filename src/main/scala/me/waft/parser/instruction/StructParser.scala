package me.waft.parser.instruction

import fastparse.noApi._
import me.waft.parser.WhiteSpaceApi._
import me.waft.parser.SILOperandParser._
import me.waft.parser.SILDeclRefParser._
import me.waft.sil.instruction.StructExtract

object StructParser {
  def structExtract: P[StructExtract] =
    ("struct_extract" ~ silOperand ~ "," ~ silDeclRef).map(StructExtract.tupled)
}
