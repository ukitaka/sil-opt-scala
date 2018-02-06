import me.waft.parser.{SILBasicBlockParser, SILInstructionParser}
import me.waft.sil.instruction.AllocStack
import me.waft.sil._
import org.scalatest._

class SILInstructionParserSpec extends FlatSpec with Matchers {
  "SIL instruction parser" should "work well with alloc_stack" in {
    val sil = "alloc_stack $Dog"
    val result = SILInstructionParser.silInstruction.parse(sil).get.value
    result should be (AllocStack(SILType("$Dog")))
  }
  "SIL instruction def parser" should "work well with alloc_stack" in {
    val sil = "%1 = alloc_stack $Dog"
    val result = SILInstructionParser.silInstructionDef.parse(sil).get.value
    result.instruction should be (AllocStack(SILType("$Dog")))
    result.values.head should be (SILValue("%1"))
  }
}
