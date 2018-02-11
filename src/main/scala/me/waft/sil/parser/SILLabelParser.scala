package me.waft.sil.parser

import fastparse.noApi._
import me.waft.sil.lang.{SILLabel, SILOperand}

trait SILLabelParser extends IdentifierParser with SILOperandParser with SILTypeParser {
  import WhiteSpaceApi._

  def silLabel: P[SILLabel] =
    (identifier ~ silLabelArguments ~ ":")
      .map(SILLabel.tupled)

  private[this] def silLabelArguments: P[Seq[SILOperand]] =
    silOperand.repTC(1).parened.??
}
