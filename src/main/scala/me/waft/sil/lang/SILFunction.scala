package me.waft.sil.lang

import me.waft.sil.lang.decl.SILDecl

case class SILFunction(linkage: Option[SILLinkage],
                       name: String,
                       `type`: SILType,
                       basicBlocks: Seq[SILBasicBlock])
    extends SILDecl {

  val entryBB: SILBasicBlock = basicBlocks.head

  val canonicalExitBB: SILBasicBlock = basicBlocks
    .filter(_.terminator.isReturn)
    .headOption
    .getOrElse(basicBlocks.last)

  val statements: Seq[SILStatement] = basicBlocks.flatMap(_.statements)

  def declaredStatement(value: SILValue): Option[SILStatement] =
    (for {
      bb <- basicBlocks
      i <- bb.instructionDefs if i.values.contains(value)
    } yield SILStatement(i, bb)).headOption
}
