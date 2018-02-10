package me.waft.sil.parser

import fastparse.noApi._
import me.waft.core.parser.Parser
import me.waft.sil.lang.decl.SILStageDecl

trait SILStageDeclParser extends Parser {
  import WhiteSpaceApi._

  // sil-stage-decl ::= 'sil_stage' sil-stage
  def silStageDecl: P[SILStageDecl] = P("sil_stage" ~ silStage)

  private[this] def silStage: P[SILStageDecl] = P(raw | canonical)

  // sil-stage ::= 'raw'
  private[this] def raw: P[SILStageDecl] =
    P("raw").const(SILStageDecl.Raw)

  // sil-stage ::= 'canonical'
  private[this] def canonical: P[SILStageDecl] =
    P("canonical").const(SILStageDecl.Canonical)
}
