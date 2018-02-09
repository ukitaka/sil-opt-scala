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
        .map(_ => SILDeclSubref.apply(None, None, Some(SILDeclLang.Foreign)))

  import SILDeclSubrefPart._

  def silDeclSubrefPart: P[SILDeclSubrefPart] = {
    return P("getter").map(_ => Getter) |
      P("setter").map(_ => Setter) |
      P("allocator").map(_ => Allocator) |
      P("initializer").map(_ => Initializer) |
      P("enumelt").map(_ => Enumelt) |
      P("destroyer").map(_ => Destoryer) |
      P("deallocator").map(_ => Deallocator) |
      P("globalaccessor").map(_ => Globalaccessor) |
      P("ivardestroyer").map(_ => Ivardestroyer) |
      P("ivarinitializer").map(_ => Ivarinitializer) |
      P("defaultarg" ~ "." ~ number).map(Defaultarg)
  }

  def silDeclUncurryLevel: P[SILDeclUncurryLevel] =
    number.map(SILDeclUncurryLevel.apply)

  def silDeclLang: P[SILDeclLang] =
    P("foreign").map(_ => SILDeclLang.Foreign)

  def number: P[Int] = CharIn('0' to '9').rep(1).!.map(_.toInt)
}
