package me.waft.parser

import fastparse.noApi._
import White._
import me.waft.sil._
import SILOperandParser._
import SILBasicBlockParser._

object SILTerminatorParser {
  def silTerminator: P[SILTerminator] = unreachable | `return` | `throw` | unwind | br | condBr

  private[this] def unreachable: P[SILTerminator] = P("unreachable").map(_ => Unreachable)

  private[this] def `return`: P[Return] = P("return" ~ silOperand).map(Return)

  private[this] def `throw`: P[Throw] = P("throw" ~ silOperand).map(Throw)

  private[this] def unwind: P[SILTerminator] = P("unwind").map(_ => Unwind)

  private[this] def br: P[Br] = P("br" ~ silLabel ~ silOperand.rep(0)).map(Br.tupled)

  private[this] def condBr: P[CondBr] = (
    // condition
    "cond_br" ~ silOperand ~ "," ~
      // if true
      silLabel ~
      ( "(" ~ silOperand ~ ( "," ~ silOperand ).rep(1) ~ ")" ).map(ops => ops._1 +: ops._2) ~
      // if false
      silLabel ~
      ( "(" ~ silOperand ~ ( "," ~ silOperand ).rep(1) ~ ")" ).map(ops => ops._1 +: ops._2)
    ).map(CondBr.tupled)
}
