import fastparse.core.Parsed
import me.waft.parser.SwiftTypeParser
import me.waft.swift.`type`.{Attribute, FunctionTypeArgument, NominalType, TupleType}
import org.scalatest._

class SwiftFunctionTypeParserSpec extends FlatSpec with Matchers {
  "attribute parsing" should "work well" in {
    val attr = "@convention(thin)"
    val result = SwiftTypeParser.attribute.parse(attr).get.value
    result should be (Attribute("convention", Seq("thin")))
  }

  "attributes parsing" should "work well" in {
    val attr = "@convention(thin)"
    val result = SwiftTypeParser.attributes.parse(attr).get.value
    result should be (Seq(Attribute("convention", Seq("thin"))))
  }

  "nominal type parsing" should "work well" in {
    val boolType = "Bool"
    val result = SwiftTypeParser.nominalType.parse(boolType).get.value
    result should be (NominalType("Bool"))
  }

  "nominal type parsing" should "be fail with type wrapped with ( )" in {
    val boolType = "(Bool)"
    val result = SwiftTypeParser.nominalType.parse(boolType)
    result should matchPattern { case Parsed.Failure(_, _, _) =>  }
  }

  "functionTypeArgumentClause parsing" should "be fail without ->" in {
    val boolType = "Bool"
    val result = SwiftTypeParser.functionTypeArgumentClause.parse(boolType)
    result should matchPattern { case Parsed.Failure(_, _, _) => }
  }

  "function type parsing" should "be fail without ->" in {
    val boolType = "Bool"
    val result = SwiftTypeParser.functionType.parse(boolType)
    result should matchPattern { case Parsed.Failure(_, _, _) => }
  }

  "functionTypeArgumentClause" should "be parsed well" in {
    val arg = "(Bool)"
    val result = SwiftTypeParser.functionTypeArgumentClause.parse(arg).get.value
    result should be (TupleType(List(FunctionTypeArgument(List(),NominalType("Bool")))))
  }

  "function type parsing" should "work well" in {
    val swiftType = "(Bool) -> Bool"
    val result = SwiftTypeParser.functionType.parse(swiftType).get.value
    result.valueType should be (NominalType("Bool"))
  }

  "function parsing" should "work well" in {
    val swiftType = "@convention(thin) (Bool) -> Bool"
    val result = SwiftTypeParser.functionType.parse(swiftType).get.value
    result.valueType should be (NominalType("Bool"))
    result.argType should matchPattern { case TupleType(_) => }
  }
}
