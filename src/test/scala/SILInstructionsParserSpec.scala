import org.scalatest._
import me.waft.parser.instruction.StructParser._
import me.waft.sil.decl.SILDeclRef
import me.waft.sil.{SILOperand, SILType, SILValue}

class SILInstructionsParserSpec extends FlatSpec with Matchers {
  "struct_extract instruction" should "be parsed well" in {
    val instruction = "struct_extract %0 : $Bool, #Bool._value"
    val result = structExtract.parse(instruction).get.value
    result.operand should be(SILOperand(SILValue("%0"), SILType("Bool")))
    result.declRef should be(SILDeclRef("Bool._value", None))
  }
}
