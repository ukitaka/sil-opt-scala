package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.instruction.{Struct, StructExtract}
import me.waft.sil.parser.{SILDeclRefParser, SILOperandParser, SILTypeParser}

trait AggregateTypesParser extends SILDeclRefParser
    with SILOperandParser
    with SILTypeParser {
  import WhiteSpaceApi._
  def structExtract: P[StructExtract] =
    ("struct_extract" ~ silOperand ~ "," ~ silDeclRef).map(StructExtract.tupled)

  def struct: P[Struct] =
    ("struct" ~ silType ~ silOperand.repTC(1).parened).map(Struct.tupled)
}
