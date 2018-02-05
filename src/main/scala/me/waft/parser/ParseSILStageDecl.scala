package me.waft.parser

import fastparse.all._
import me.waft.sil.decl.SILStageDecl

object ParseSILStageDecl {
  // sil-stage-decl ::= 'sil_stage' sil-stage
  def silStageDecl: P[SILStageDecl] = P("sil_stage " ~ silStage)

  private[this] def silStage: P[SILStageDecl] = P(raw | canonical)

  // sil-stage ::= 'raw'
  private[this] def raw: P[SILStageDecl] =
    P("raw").map(_ => SILStageDecl.Raw)

  // sil-stage ::= 'canonical'
  private[this] def canonical: P[SILStageDecl] =
    P("canonical").map(_ => SILStageDecl.Canonical)
}
