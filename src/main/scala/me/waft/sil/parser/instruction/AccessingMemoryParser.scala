package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.parser.WhiteSpaceApi._
import me.waft.sil.lang.instruction.{Load, Store}
import me.waft.sil.parser.SILOperandParser._
import me.waft.sil.parser.SILValueParser._

trait AccessingMemoryParser {
  def store: P[Store] = ("store" ~ silValue ~ "to" ~ silOperand).map(Store.tupled)
  def load: P[Load] = ("load" ~ silOperand).map(Load)
}
