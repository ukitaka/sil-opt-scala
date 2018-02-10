package me.waft.sil.parser

import fastparse.noApi._
import me.waft.core.parser.Parser
import me.waft.sil.lang.SILLinkage

trait SILLinkageParser extends Parser {
  import WhiteSpaceApi._

  def silLinkage: P[SILLinkage] =
    ("public" | "hidden" | "shared" | "private" |
      "public_external" | "hidden_external" ).!.map(SILLinkage.apply)
}
