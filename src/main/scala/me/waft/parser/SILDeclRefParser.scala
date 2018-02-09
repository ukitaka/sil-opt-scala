package me.waft.parser
import fastparse.noApi._
import WhiteSpaceApi._
import me.waft.sil.decl._
import IdentifierParser.SIL

object SILDeclRefParser {
  def silDeclRef: P[SILDeclRef] =
    ("#" ~ silDeclRefIdentifier ~ silDeclSubref.?).map(SILDeclRef.tupled)

  def silDeclRefIdentifier: P[String] = ( SIL.identifier ~ (".".! ~ SIL.identifier)).!

  def silDeclSubref: P[SILDeclSubref] =
    ("!" ~ silDeclSubrefPart ~ ("." ~ silDeclUncurryLevel).? ~ ("." ~ silDeclLang).?)
      .map(a => SILDeclSubref.apply(Some(a._1), a._2, a._3)) |
    ("!" ~ silDeclUncurryLevel ~ ("." ~ silDeclLang).?)
        .map(a => SILDeclSubref.apply(None, Some(a._1), a._2)) |
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

  def number: P[Int] = CharIn('0' to '9').rep(1).!.map(_.toInt)
}
