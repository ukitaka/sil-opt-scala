package me.waft.swift.parser

import fastparse.core.Parsed
import me.waft.swift.lang.`type`.GenericParameter.ConformanceRequirement
import me.waft.swift.lang.`type`._
import org.scalatest._

class SwiftTypeParserSpec extends FlatSpec with Matchers with SwiftTypeParser {
  "attribute parsing" should "work well" in {
    val attr = "@convention(thin)"
    val result = attribute.parse(attr).get.value
    result should be(Attribute("convention", Seq("thin")))
  }

  "attributes parsing" should "work well" in {
    val attr = "@convention(thin)"
    val result = attributes.parse(attr).get.value
    result should be(Seq(Attribute("convention", Seq("thin"))))
  }

  "nominal type parsing" should "work well" in {
    val boolType = "Bool"
    val result = nominalType.parse(boolType).get.value
    result should be(NominalType("Bool"))
  }

  "nominal type parsing" should "work well with type including dots" in {
    val boolType = "A.B"
    val result = nominalType.parse(boolType).get.value
    result should be(NominalType("A.B"))
  }

  "nominal type parsing" should "be fail with type wrapped with ( )" in {
    val boolType = "(Bool)"
    val result = nominalType.parse(boolType)
    result should matchPattern { case Parsed.Failure(_, _, _) => }
  }

  "nominal type parsing" should "work well with space" in {
    val s = "Dog Cat"
    val result = nominalType.parse(s).get.value
    result should be(NominalType("Dog"))
  }

  "functionTypeArgumentClause parsing" should "be fail without ->" in {
    val boolType = "Bool"
    val result = functionTypeArgumentClause.parse(boolType)
    result should matchPattern { case Parsed.Failure(_, _, _) => }
  }

  "function type parsing" should "be fail without ->" in {
    val boolType = "Bool"
    val result = functionType.parse(boolType)
    result should matchPattern { case Parsed.Failure(_, _, _) => }
  }

  "functionTypeArgumentClause" should "be parsed well" in {
    val arg = "(Bool)"
    val result = functionTypeArgumentClause.parse(arg).get.value
    result should be(
      TupleType(List(FunctionTypeArgument(List(), NominalType("Bool")))))
  }

  "function type parsing" should "work well" in {
    val swiftType = "(Bool) -> Bool"
    val result = functionType.parse(swiftType).get.value
    result.valueType should be(NominalType("Bool"))
  }

  "function type parsing" should "work well with empty tuple" in {
    val swiftType = "() -> ()"
    val result = functionType.parse(swiftType).get.value
    result.valueType should be(TupleType(Seq()))
    result.argType should be(TupleType(Seq()))
  }

  "function parsing" should "work well" in {
    val swiftType = "@convention(thin) (Bool) -> Bool"
    val result = functionType.parse(swiftType).get.value
    result.valueType should be(NominalType("Bool"))
    result.argType should matchPattern { case TupleType(_) => }
    result.attributes.head.name should be("convention")
    result.attributes.head.balancedTokens.head should be("thin")
  }

  "@opened attributed type" should "be parsed well" in {
    val swiftType = """@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto"""
    val result = annotatedType.parse(swiftType).get.value
    result.`type` should be(NominalType("Proto"))
    result.attributes.head.name should be("opened")
    result.attributes.head.balancedTokens.head should be(
      "1B68354A-4796-11E6-B7DF-B8E856428C60")
  }

  "attributed function type that has generic params" should "be parsed well" in {
    val attr = """@convention(witness_method: Pingable)"""
    val res1 = attribute.parse(attr).get.value
    res1.name should be("convention")
    res1.balancedTokens.head should be("witness_method: Pingable")

    val gen = """<τ_0_0 where τ_0_0 : Pingable>"""
    val res2 = genericParameterClause.parse(gen).get.value
    res2.typeParameters.head should be(NominalType("τ_0_0"))
    res2.requirements.head should be(
      ConformanceRequirement(NominalType("τ_0_0"), NominalType("Pingable")))

    val attr2 = """@in_guaranteed"""
    val res3 = attributes.parse(attr2).get.value.head
    res3.name should be("in_guaranteed")

    val argType =
      FunctionTypeArgument(Seq(Attribute("in_guaranteed", Seq())),
        NominalType("τ_0_0"))

    val an = """(@in_guaranteed τ_0_0)"""
    val res4 = functionTypeArgumentClause.parse(an).get.value
    res4.types.head should be(argType)

    val func = """(@in_guaranteed τ_0_0) -> ()"""
    val res5 = functionType.parse(func).get.value
    res5.argType should be(TupleType(Seq(argType)))
    res5.valueType should be(TupleType(Seq()))

    val s = """@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> ()"""
    val result = genericFunctionType.parse(s).get.value
    result.argType should be(TupleType(Seq(argType)))
    result.valueType should be(TupleType(Seq()))
  }
}
