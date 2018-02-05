package me.waft.parser

import fastparse.all._
import SILValueParser._

object SILInstructionParser {
    def silInstructionResult: P[Seq[String]] =
        ("(" ~ silValueName ~ silInstructionResultNames ~ ")").map(names => names._1 +: names._2)

    private[this] def silInstructionResultNames: P[Seq[String]] = {
        ("," ~ silValueName).rep(0).?.map(_.getOrElse(Seq.empty))
    }
}
