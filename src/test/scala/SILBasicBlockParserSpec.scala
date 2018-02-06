import me.waft.parser.SILBasicBlockParser
import me.waft.sil._
import org.scalatest._

class SILBasicBlockParserSpec extends FlatSpec with Matchers {
  "SIL basic block parser" should "work well" in {
    val sil =
      """|bb0(%0 : $Dog):
         |  return %1 : $Dog
       """.stripMargin
    val result = SILBasicBlockParser.basicBlock.parse(sil).get.value
    result.label.identifier should be ("bb0")
    result.label.args.head should be (SILArgument("%0", SILType("$Dog")))
    result.terminator should be (Return(SILOperand(SILValue("%1"), SILType("$Dog"))))
  }
}