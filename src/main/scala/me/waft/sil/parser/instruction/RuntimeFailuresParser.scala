package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang.CondFail
import me.waft.sil.parser.SILOperandParser

trait RuntimeFailuresParser extends SILOperandParser {

  import WhiteSpaceApi._

  def condFail: P[CondFail] =
    ("cond_fail" ~ silOperand).map(CondFail)
}
