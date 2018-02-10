package me.waft.swift.parser

import fastparse.core.Parsed
import me.waft.swift.lang.`type`.{Attribute, FunctionTypeArgument, NominalType, TupleType}
import org.scalatest._

class SwiftFunctionTypeParserSpec extends FlatSpec with Matchers with SwiftTypeParser {
  "attribute parsing" should "work well" in {
    val attr = "@convention(thin)"
    val result = attribute.parse(attr).get.value
    result should be (Attribute("convention", Seq("thin")))
  }

  "attributes parsing" should "work well" in {
    val attr = "@convention(thin)"
    val result = attributes.parse(attr).get.value
    result should be (Seq(Attribute("convention", Seq("thin"))))
  }

  "nominal type parsing" should "work well" in {
    val boolType = "Bool"
    val result = nominalType.parse(boolType).get.value
    result should be (NominalType("Bool"))
  }

  "nominal type parsing" should "work well with type including dots" in {
    val boolType = "A.B"
    val result = nominalType.parse(boolType).get.value
    result should be (NominalType("A.B"))
  }

  "nominal type parsing" should "be fail with type wrapped with ( )" in {
    val boolType = "(Bool)"
    val result = nominalType.parse(boolType)
    result should matchPattern { case Parsed.Failure(_, _, _) =>  }
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
    result should be (TupleType(List(FunctionTypeArgument(List(),NominalType("Bool")))))
  }

  "function type parsing" should "work well" in {
    val swiftType = "(Bool) -> Bool"
    val result = functionType.parse(swiftType).get.value
    result.valueType should be (NominalType("Bool"))
  }

  "function parsing" should "work well" in {
    val swiftType = "@convention(thin) (Bool) -> Bool"
    val result = functionType.parse(swiftType).get.value
    result.valueType should be (NominalType("Bool"))
    result.argType should matchPattern { case TupleType(_) => }
    result.attributes.head.name should be("convention")
    result.attributes.head.balancedTokens.head should be("thin")
  }
}
