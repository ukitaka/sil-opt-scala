package me.waft.parser

import fastparse.noApi._
import me.waft.parser.WhiteSpaceApi._
import SwiftTypeParser._
import me.waft.sil.lang.SILType

object SILTypeParser {
  def silType: P[SILType] = ("$" ~ "*".? ~ `type`).map(SILType.apply)
}
