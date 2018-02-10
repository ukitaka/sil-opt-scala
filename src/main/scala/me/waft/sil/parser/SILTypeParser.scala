package me.waft.sil.parser

import fastparse.noApi._
import me.waft.core.parser.Parser
import me.waft.swift.parser.SwiftTypeParser
import me.waft.sil.lang.SILType

trait SILTypeParser extends Parser with SwiftTypeParser {
  import WhiteSpaceApi._
  def silType: P[SILType] = ("$" ~~ "*".? ~~ swiftType).map(SILType.apply)
}
