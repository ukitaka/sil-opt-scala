package me.waft.sil.parser
import fastparse.noApi._
import me.waft.sil.lang.decl
import me.waft.sil.lang.decl._

trait SILDeclRefParser extends IdentifierParser {
  import WhiteSpaceApi._

  def silDeclRef: P[SILDeclRef] =
    ("#" ~ silDeclRefIdentifier ~ silDeclSubref.?).map(SILDeclRef.tupled)

  def silDeclRefIdentifier: P[String] = ( identifier ~ (".".! ~ identifier)).!

  def silDeclSubref: P[SILDeclSubref] =
    ("!" ~ silDeclSubrefPart ~ ("." ~ silDeclUncurryLevel).? ~ ("." ~ silDeclLang).?)
      .map { case (part, level, lang) => decl.SILDeclSubref(Some(part), level, lang) } |
    ("!" ~ silDeclUncurryLevel ~ ("." ~ silDeclLang).?)
      .map { case (level, lang) => decl.SILDeclSubref(None, Some(level), lang) } |
    ("!" ~ silDeclLang)
        .const(SILDeclSubref(None, None, Some(SILDeclLang.Foreign)))

  import SILDeclSubrefPart._

  def silDeclSubrefPart: P[SILDeclSubrefPart] = {
    return P("getter").const(Getter) |
      P("setter").const(Setter) |
      P("allocator").const(Allocator) |
      P("initializer").const(Initializer) |
      P("enumelt").const(Enumelt) |
      P("destroyer").const(Destoryer) |
      P("deallocator").const(Deallocator) |
      P("globalaccessor").const(Globalaccessor) |
      P("ivardestroyer").const(Ivardestroyer) |
      P("ivarinitializer").const(Ivarinitializer) |
      P("defaultarg" ~ "." ~ number).map(Defaultarg)
  }

  def silDeclUncurryLevel: P[SILDeclUncurryLevel] =
    number.map(SILDeclUncurryLevel)

  def silDeclLang: P[SILDeclLang] =
    P("foreign").const(SILDeclLang.Foreign)
}
