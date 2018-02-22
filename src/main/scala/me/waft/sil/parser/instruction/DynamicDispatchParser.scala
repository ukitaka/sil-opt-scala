package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang._
import me.waft.sil.parser.{SILDeclRefParser, SILOperandParser}

trait DynamicDispatchParser extends SILOperandParser with SILDeclRefParser {
  import WhiteSpaceApi._

  def witnessMethod: P[WitnessMethod] =
    ("witness_method" ~ silType ~ "," ~ silDeclRef ~ ":" ~ silType)
      .map(WitnessMethod.tupled)
}
