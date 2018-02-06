package me.waft.parser

import fastparse.noApi._
import me.waft.parser.White._
import me.waft.sil.SILLinkage

object SILLinkageParser {
  def silLinkage: P[SILLinkage] =
    ("public" | "hidden" | "shared" | "private" |
      "public_external" | "hidden_external" ).!.map(SILLinkage.apply)
}
