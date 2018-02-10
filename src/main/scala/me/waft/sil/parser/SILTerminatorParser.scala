package me.waft.sil.parser

import fastparse.noApi._
import me.waft.sil.lang._

trait SILTerminatorParser extends SILOperandParser with SILLabelParser {
  import WhiteSpaceApi._

  def silTerminator: P[SILTerminator] = unreachable | `return` | `throw` | unwind | br | condBr

  private[this] def unreachable: P[SILTerminator] = P("unreachable").const(Unreachable)

  private[this] def `return`: P[Return] = P("return" ~ silOperand).map(Return)

  private[this] def `throw`: P[Throw] = P("throw" ~ silOperand).map(Throw)

  private[this] def unwind: P[SILTerminator] = P("unwind").const(Unwind)

  private[this] def br: P[Br] = P("br" ~ silLabel ~ silOperand.rep(0)).map(Br.tupled)

  private[this] def condBr: P[CondBr] = (
    // condition
    "cond_br" ~ silOperand ~ "," ~
      // if true
      silLabel ~
      (silOperand.repTC(1).parened) ~
      // if false
      silLabel ~
      (silOperand.repTC(1).parened)
    ).map(CondBr.tupled)
}