import me.waft.parser.SwiftTypeParser
import org.scalatest._

class SwiftFunctionTypeParserSpec extends FlatSpec with Matchers {
  "function parsing" should "work well" in {
    val swiftType = "@convention(thin) (Bool) -> Bool"
    val result = SwiftTypeParser.`type`.parse(swiftType).get.value
    true should be (false)
  }
}
