package me.waft.sil.optimizer.pass

import me.waft.sil.emitter.SILEmitter
import me.waft.sil.parser.SILFunctionParser
import org.scalatest.{FlatSpec, Matchers}

class CSESpec extends FlatSpec with Matchers with SILFunctionParser {
  val optimizer = it

  optimizer should "optimize @test0 well" in {
    val sil =
      """|sil @test0 : $@convention(thin) () -> (Builtin.Int8, Builtin.Int8, Builtin.Int16,
         |                           Builtin.Int8) {
         |bb0:
         |    %0 = integer_literal $Builtin.Int8, 8
         |    %1 = integer_literal $Builtin.Int8, 8
         |    %2 = integer_literal $Builtin.Int16, 8
         |    %3 = integer_literal $Builtin.Int8, 1
         |    %4 = tuple(%0 : $Builtin.Int8, %1 : $Builtin.Int8, %2 : $Builtin.Int16, %3 : $Builtin.Int8)
         |    return %4 :  $(Builtin.Int8, Builtin.Int8, Builtin.Int16, Builtin.Int8)
         |}
      """.stripMargin

    val optimizedSil =
      """|sil @test0 : $@convention(thin) () -> (Builtin.Int8, Builtin.Int8, Builtin.Int16, Builtin.Int8) {
         |bb0:
         |  %0 = integer_literal $Builtin.Int8, 8
         |  %1 = integer_literal $Builtin.Int16, 8
         |  %2 = integer_literal $Builtin.Int8, 1
         |  %3 = tuple (%0 : $Builtin.Int8, %0 : $Builtin.Int8, %1 : $Builtin.Int16, %2 : $Builtin.Int8)
         |  return %3 : $(Builtin.Int8, Builtin.Int8, Builtin.Int16, Builtin.Int8)
         |}
      """.stripMargin

    val original = silFunction.parse(sil).get.value
    val expected = silFunction.parse(optimizedSil).get.value
    val optimized = CSE.run(original)

    optimized should be(expected)
  }

  optimizer should "optimize @test1 well" in {
    val sil =
      """|sil @test1 : $@convention(thin) () -> (Builtin.Int8, Builtin.Int8, Builtin.Int8) {
         |bb0:
         |    %0 = integer_literal $Builtin.Int8, 8
         |    %1 = integer_literal $Builtin.Int8, 8
         |    cond_br undef, bb1, bb2
         |
         |bb1:
         |    %2 = integer_literal $Builtin.Int8, 8
         |    br bb3(%2 : $Builtin.Int8)
         |
         |bb2:
         |    %3 = integer_literal $Builtin.Int8, 16
         |    br bb3(%3 : $Builtin.Int8)
         |
         |bb3(%4 : $Builtin.Int8):
         |    %5 = tuple(%0 : $Builtin.Int8, %4 : $Builtin.Int8, %1 : $Builtin.Int8)
         |    return %5 : $(Builtin.Int8, Builtin.Int8, Builtin.Int8)
         |}
      """.stripMargin

    val optimizedSil =
      """|sil @test1 : $@convention(thin) () -> (Builtin.Int8, Builtin.Int8, Builtin.Int8) {
         |bb0:
         |  %0 = integer_literal $Builtin.Int8, 8
         |  cond_br undef, bb1, bb2
         |bb1:
         |  br bb3(%0 : $Builtin.Int8)
         |bb2:
         |  %3 = integer_literal $Builtin.Int8, 16
         |  br bb3(%3 : $Builtin.Int8)
         |bb3(%5 : $Builtin.Int8):
         |  %6 = tuple (%0 : $Builtin.Int8, %5 : $Builtin.Int8, %0 : $Builtin.Int8)
         |  return %6 : $(Builtin.Int8, Builtin.Int8, Builtin.Int8)
         |}
      """.stripMargin

    val original = silFunction.parse(sil).get.value
    val expected = silFunction.parse(optimizedSil).get.value
    val optimized = CSE.run(original)

    optimized should be(expected)
  }

  ignore should "optimize open_existential_addr instruction" in {
      val sil =
        """|sil hidden @_TF2p28trytoflyFPS_7Flyable_T_ : $@convention(thin) (@in Flyable) -> () {
           |bb0(%0 : $*Flyable):
           |  %2 = open_existential_addr immutable_access %0 : $*Flyable to $*@opened("D8A4A5D8-4C44-11E5-BA43-AC87A3294C0A") Flyable // users: %3, %4
           |  %3 = witness_method $@opened("D8A4A5D8-4C44-11E5-BA43-AC87A3294C0A") Flyable, #Flyable.fly!1, %2 : $*@opened("D8A4A5D8-4C44-11E5-BA43-AC87A3294C0A") Flyable : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> () // user: %4
           |  %4 = apply %3<@opened("D8A4A5D8-4C44-11E5-BA43-AC87A3294C0A") Flyable>(%2) : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> ()
           |  %5 = open_existential_addr immutable_access %0 : $*Flyable to $*@opened("D8A4B49C-4C44-11E5-BA43-AC87A3294C0A") Flyable // users: %6, %7
           |  %6 = witness_method $@opened("D8A4B49C-4C44-11E5-BA43-AC87A3294C0A") Flyable, #Flyable.fly!1, %5 : $*@opened("D8A4B49C-4C44-11E5-BA43-AC87A3294C0A") Flyable : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> () // user: %7
           |  %7 = apply %6<@opened("D8A4B49C-4C44-11E5-BA43-AC87A3294C0A") Flyable>(%5) : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> ()
           |  %8 = open_existential_addr immutable_access %0 : $*Flyable to $*@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable // users: %9, %10
           |  %9 = witness_method $@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable, #Flyable.fly!1, %8 : $*@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> () // user: %10
           |  %10 = apply %9<@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable>(%8) : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> ()
           |  %11 = witness_method $@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable, #Flyable.fly!1, %8 : $*@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> () // user: %12   %12 = apply %9<@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable>(%8) : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> ()
           |  destroy_addr %0 : $*Flyable                     // id: %11
           |  %13 = tuple ()                                  // user: %13
           |  return %13 : $()                                // id: %13
           |}
        """.stripMargin

      val optimizedSil =
        """|sil hidden @_TF2p28trytoflyFPS_7Flyable_T_ : $@convention(thin) (@in Flyable) -> () {
           |// %0                                             // users: %11, %7, %4, %1
           |bb0(%0 : $*Flyable):
           |  %1 = open_existential_addr immutable_access %0 : $*Flyable to $*@opened("D8A4A5D8-4C44-11E5-BA43-AC87A3294C0A") Flyable // users: %6, %6, %3, %3, %2
           |  %2 = witness_method $@opened("D8A4A5D8-4C44-11E5-BA43-AC87A3294C0A") Flyable, #Flyable.fly!1 : <Self where Self : Flyable> (Self) -> () -> (), %1 : $*@opened("D8A4A5D8-4C44-11E5-BA43-AC87A3294C0A") Flyable : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> () // type-defs: %1; users: %6, %3
           |  %3 = apply %2<@opened("D8A4A5D8-4C44-11E5-BA43-AC87A3294C0A") Flyable>(%1) : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> () // type-defs: %1
           |  %4 = open_existential_addr immutable_access %0 : $*Flyable to $*@opened("D8A4B49C-4C44-11E5-BA43-AC87A3294C0A") Flyable // user: %5
           |  %5 = witness_method $@opened("D8A4B49C-4C44-11E5-BA43-AC87A3294C0A") Flyable, #Flyable.fly!1 : <Self where Self : Flyable> (Self) -> () -> (), %4 : $*@opened("D8A4B49C-4C44-11E5-BA43-AC87A3294C0A") Flyable : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> () // type-defs: %4
           |  %6 = apply %2<@opened("D8A4A5D8-4C44-11E5-BA43-AC87A3294C0A") Flyable>(%1) : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> () // type-defs: %1
           |  %7 = open_existential_addr immutable_access %0 : $*Flyable to $*@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable // users: %10, %10, %9, %9, %8
           |  %8 = witness_method $@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable, #Flyable.fly!1 : <Self where Self : Flyable> (Self) -> () -> (), %7 : $*@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> () // type-defs: %7; users: %10, %9
           |  %9 = apply %8<@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable>(%7) : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> () // type-defs: %7
           |  %10 = apply %8<@opened("D8A4BCB2-4C44-11E5-BA43-AC87A3294C0A") Flyable>(%7) : $@convention(witness_method: Flyable) <τ_0_0 where τ_0_0 : Flyable> (@in_guaranteed τ_0_0) -> () // type-defs: %7
           |  destroy_addr %0 : $*Flyable                     // id: %11
           |  %12 = tuple ()                                  // user: %13
           |  return %12 : $()                                // id: %13
           |}
        """.stripMargin

      val original = silFunction.parse(sil).get.value
      val expected = silFunction.parse(optimizedSil).get.value
      val optimized = CSE.run(original)
      optimized should be(expected)
  }



}
