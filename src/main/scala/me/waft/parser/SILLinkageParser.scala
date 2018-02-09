package me.waft.parser

import fastparse.noApi._
import me.waft.parser.WhiteSpaceApi._
import me.waft.lang.SILLinkage

object SILLinkageParser {
  def silLinkage: P[SILLinkage] =
    ("public" | "hidden" | "shared" | "private" |
      "public_external" | "hidden_external" ).!.map(SILLinkage.apply)
}
