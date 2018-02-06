import me.waft.parser.SILBasicBlockParser
import me.waft.sil.instruction.AllocStack
import me.waft.sil._
import org.scalatest._

class SILBasicBlockParserSpec extends FlatSpec with Matchers {
  "SIL basic block parser" should "work well" in {
    val sil =
      """|bb0(%0 : $Dog):
         |  %1 = alloc_stack $Dog
         |  return %1 : $Dog
       """.stripMargin
    val result = SILBasicBlockParser.basicBlock.parse(sil).get.value
    result.label.identifier should be ("bb0")
    result.label.args.head should be (SILArgument("%0", SILType("$Dog")))
    result.instructionDefs.head.instruction should be (AllocStack(SILType("$Dog")))
    result.instructionDefs.head.values.head should be (SILValue("%1"))
    result.terminator should be (Return(SILOperand(SILValue("%1"), SILType("$Dog"))))
  }
}