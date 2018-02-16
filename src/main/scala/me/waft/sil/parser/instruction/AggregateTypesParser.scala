package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.{Struct, StructExtract, Tuple, TupleExtract}
import me.waft.sil.parser.{SILDeclRefParser, SILOperandParser, SILTypeParser}

trait AggregateTypesParser extends SILDeclRefParser
  with SILOperandParser
  with SILTypeParser {

  import WhiteSpaceApi._

  def structExtract: P[StructExtract] =
    ("struct_extract" ~ silOperand ~ "," ~ silDeclRef).map(StructExtract.tupled)

  def struct: P[Struct] =
    ("struct" ~ silType ~ silOperand.repTC(1).parened).map(Struct.tupled)

  //TODO: parser does not works in case of ↓
  // sil-type '(' (sil-value (',' sil-value)*)? ')'
  def tuple: P[Tuple] =
    ("tuple" ~ silOperand.repTC(0).parened).map(Tuple.apply)

  def tupleExtract: P[TupleExtract] =
    ("tuple_extract" ~ silOperand ~ "," ~ intLiteral).map(TupleExtract.tupled)
}
