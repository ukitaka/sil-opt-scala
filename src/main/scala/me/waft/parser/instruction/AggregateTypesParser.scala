package me.waft.parser.instruction

import fastparse.noApi._
import me.waft.parser.SILDeclRefParser._
import me.waft.parser.SILOperandParser._
import me.waft.parser.SILTypeParser._
import me.waft.parser.WhiteSpaceApi._
import me.waft.sil.lang.instruction.{Struct, StructExtract}

trait AggregateTypesParser {
  def structExtract: P[StructExtract] =
    ("struct_extract" ~ silOperand ~ "," ~ silDeclRef).map(StructExtract.tupled)

  def struct: P[Struct] =
    ("struct" ~ silType ~ silOperand.repTC(1).parened).map(Struct.tupled)
}
