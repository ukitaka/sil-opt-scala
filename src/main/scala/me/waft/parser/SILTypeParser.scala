package me.waft.parser

import fastparse.noApi._
import me.waft.parser.WhiteSpaceApi._
import me.waft.sil.SILType
import SwiftTypeParser._

object SILTypeParser {
  def silType: P[SILType] = ("$" ~ "*".? ~ `type`).map(SILType.apply)
}
