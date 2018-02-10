package me.waft.sil.lang

import me.waft.sil.lang.decl.SILStageDecl

case class SIL (stageDecl: SILStageDecl, functions: Seq[SILFunction])
