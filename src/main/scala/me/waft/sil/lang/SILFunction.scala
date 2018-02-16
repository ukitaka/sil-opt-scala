package me.waft.sil.lang

import me.waft.sil.lang.decl.SILDecl

case class SILFunction(linkage: Option[SILLinkage], name: String,
                       `type`: SILType,
                       basicBlocks: Seq[SILBasicBlock]) extends SILDecl {

  val entryBB: SILBasicBlock = basicBlocks.head

  val canonicalExitBB: SILBasicBlock = basicBlocks
    .filter { bb =>
      bb.terminator match {
        case Return(_) => true
        case _ => false
      }
    }
    .headOption.getOrElse(basicBlocks.last)
}
