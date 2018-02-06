import me.waft.parser.SILBasicBlockParser
import org.scalatest._

class SILBasicBlockParserSpec extends FlatSpec with Matchers {
  "SIL basic block parser" should "work well" in {
    val sil =
      """|bb0(%0 : $Dog):
         |  return %0 : $Dog
       """.stripMargin
    val result = SILBasicBlockParser.basicBlock.parse(sil).get.value
    result.label.identifier should be ("bb0")
  }
}