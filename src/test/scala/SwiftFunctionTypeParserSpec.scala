import fastparse.core.Parsed
import me.waft.parser.SwiftTypeParser
import me.waft.swift.`type`.{Attribute, NominalType}
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

  "function parsing" should "work well" in {
    val swiftType = "(Bool) -> Bool"
    val result = SwiftTypeParser.functionType.parse(swiftType).get.value
    true should be (false)
  }

//  "function parsing" should "work well" in {
//    val swiftType = "@convention(thin) (Bool) -> Bool"
//    val result = SwiftTypeParser.`type`.parse(swiftType).get.value
//    true should be (false)
//  }
}
