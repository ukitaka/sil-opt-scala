package me.waft.sil.lang

case class SILBasicBlock(label: SILLabel, instructionDefs: Seq[SILInstructionDef], terminator: SILTerminator) {
  val statements: Seq[SILStatement] =
    instructionDefs.map(i => SILStatement(i, this)) :+ SILStatement(terminator, this)
}
