package me.waft.sil

import me.waft.sil.decl.Decl

case class SILFunction(linkage: Option[SILLinkage], name: String, `type`: SILType, basicBlocks: Seq[SILBasicBlock]) extends Decl
