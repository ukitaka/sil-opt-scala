package me.waft.lang

import me.waft.lang.decl.Decl

case class SILFunction(linkage: Option[SILLinkage], name: String, `type`: SILType, basicBlocks: Seq[SILBasicBlock]) extends Decl
