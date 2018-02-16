package me.waft.sil.lang

case class SILBasicBlock(label: SILLabel, instructionDefs: Seq[SILInstructionDef], terminator: SILTerminator) {
  val statements: Seq[SILStatement] =
    instructionDefs.map(i => SILStatement(i, this)) :+ SILStatement(terminator, this)
}

object SILBasicBlock {
  def empty(entryBB: SILBasicBlock) = SILBasicBlock(
    SILLabel("entry", entryBB.label.args),
    Seq(),
    Br(entryBB.label.identifier, entryBB.label.args)
  )
}
