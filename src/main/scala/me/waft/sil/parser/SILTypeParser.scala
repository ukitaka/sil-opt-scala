package me.waft.sil.parser

import fastparse.noApi._
import me.waft.sil.parser.WhiteSpaceApi._
import me.waft.swift.parser.SwiftTypeParser._
import me.waft.sil.lang.SILType

object SILTypeParser {
  def silType: P[SILType] = ("$" ~ "*".? ~ `type`).map(SILType.apply)
}
