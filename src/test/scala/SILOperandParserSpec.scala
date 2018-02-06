import me.waft.parser.SILOperandParser
import me.waft.sil.{SILOperand, SILType, SILValue}
import org.scalatest._

class SILOperandParserSpec extends FlatSpec with Matchers {
  "SIL operand parser" should "work well" in {
    val sil = "%1 : $*Int"
    val result = SILOperandParser.silOperand.parse(sil).get.value
    result should be (SILOperand(SILValue("%1"), SILType("$*Int")))
  }
}
