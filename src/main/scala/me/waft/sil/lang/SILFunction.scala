package me.waft.sil.lang

import me.waft.sil.lang.decl.SILDecl

case class SILFunction(linkage: Option[SILLinkage], name: String, `type`: SILType, basicBlocks: Seq[SILBasicBlock]) extends SILDecl
