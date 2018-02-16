package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.{Load, Store}
import me.waft.sil.parser.{SILOperandParser, SILValueParser}

trait AccessingMemoryParser extends SILOperandParser with SILValueParser {

  import WhiteSpaceApi._

  def store: P[Store] = ("store" ~ silValue ~ "to" ~ silOperand).map(Store.tupled)

  def load: P[Load] = ("load" ~ silOperand).map(Load)
}
