package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang._
import me.waft.sil.parser.SILOperandParser

trait DebugInformationParser extends SILOperandParser {
  import WhiteSpaceApi._

  def debugValue: P[DebugValue] = ("debug_value" ~ silOperand).map(DebugValue)

  def debugValueAddr: P[DebugValueAddr] =
    ("debug_value_addr" ~ silOperand).map(DebugValueAddr)
}
