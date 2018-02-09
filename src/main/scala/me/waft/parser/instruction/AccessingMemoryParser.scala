package me.waft.parser.instruction

import fastparse.noApi._
import me.waft.parser.WhiteSpaceApi._
import me.waft.sil.instruction.{Load, Store}
import me.waft.parser.SILValueParser._
import me.waft.parser.SILOperandParser._

trait AccessingMemoryParser {
  def store: P[Store] = ("store" ~ silValue ~ "to" ~ silOperand).map(Store.tupled)
  def load: P[Load] = ("load" ~ silOperand).map(Load)
}
