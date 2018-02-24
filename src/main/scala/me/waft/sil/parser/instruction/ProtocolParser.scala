package me.waft.sil.parser.instruction

import fastparse.noApi._
import me.waft.sil.lang._
import me.waft.sil.parser.SILOperandParser

trait ProtocolParser extends SILOperandParser {
  import WhiteSpaceApi._

  private def silAllowedAccess: P[SILAllowedAccess] =
    ("mutable_access" | "immutable_access").!.map(SILAllowedAccess.apply)

  def openExistentialAddr: P[OpenExistentialAddr] =
    ("open_existential_addr" ~ silAllowedAccess ~ silOperand ~ "to" ~ silType)
      .map(OpenExistentialAddr.tupled)

  def openExistentialValue: P[OpenExistentialValue] =
    ("open_existential_value" ~ silOperand ~ "to" ~ silType)
      .map(OpenExistentialValue.tupled)

  def openExistentialRef: P[OpenExistentialRef] =
    ("open_existential_ref" ~ silOperand ~ "to" ~ silType)
      .map(OpenExistentialRef.tupled)

  def openExistentialMetatype: P[OpenExistentialMetatype] =
    ("open_existential_metatype" ~ silOperand ~ "to" ~ silType)
      .map(OpenExistentialMetatype.tupled)
}
