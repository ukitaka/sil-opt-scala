package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.Upcast
import me.waft.sil.parser.SILOperandParser

trait UncheckedConversionsParser extends SILOperandParser {
  import WhiteSpaceApi._

  def upcast: P[Upcast] = ("upcast" ~ silOperand ~ "to" ~ silType).map(Upcast.tupled)
}
