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

  ignore should "optimize @CSE_Existential_Simple" in {
    val sil =
      """|sil hidden @CSE_Existential_Simple : $@convention(thin) (@in Pingable) -> () {
         |bb0(%0 : $*Pingable):
         |  debug_value_addr %0 : $*Pingable  // let x      // id: %1
         |  %2 = open_existential_addr immutable_access %0 : $*Pingable to $*@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable // users: %3, %4
         |  %3 = witness_method $@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable, #Pingable.ping!1, %2 : $*@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> () // user: %4
         |  %4 = apply %3<@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable>(%2) : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> ()
         |  %5 = open_existential_addr immutable_access %0 : $*Pingable to $*@opened("1E4687DC-D5C5-11E5-8C0E-A82066121073") Pingable // users: %6, %7
         |  %6 = witness_method $@opened("1E4687DC-D5C5-11E5-8C0E-A82066121073") Pingable, #Pingable.ping!1, %5 : $*@opened("1E4687DC-D5C5-11E5-8C0E-A82066121073") Pingable : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> () // user: %7
         |  %7 = apply %6<@opened("1E4687DC-D5C5-11E5-8C0E-A82066121073") Pingable>(%5) : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> ()
         |  destroy_addr %0 : $*Pingable                    // id: %8
         |  %9 = tuple ()                                   // user: %10
         |  return %9 : $()                                 // id: %10
         |}
         |
      """.stripMargin

    val optimizedSil =
      """|sil hidden @CSE_Existential_Simple : $@convention(thin) (@in Pingable) -> () {
         |bb0(%0 : $*Pingable):
         |  debug_value_addr %0 : $*Pingable                // id: %1
         |  %2 = open_existential_addr immutable_access %0 : $*Pingable to $*@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable // users: %7, %7, %4, %4, %3
         |  %3 = witness_method $@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable, #Pingable.ping!1 : <Self where Self : Pingable> (Self) -> () -> (), %2 : $*@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> () // type-defs: %2; users: %7, %4
         |  %4 = apply %3<@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable>(%2) : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> () // type-defs: %2
         |  %5 = open_existential_addr immutable_access %0 : $*Pingable to $*@opened("1E4687DC-D5C5-11E5-8C0E-A82066121073") Pingable // user: %6
         |  %6 = witness_method $@opened("1E4687DC-D5C5-11E5-8C0E-A82066121073") Pingable, #Pingable.ping!1 : <Self where Self : Pingable> (Self) -> () -> (), %5 : $*@opened("1E4687DC-D5C5-11E5-8C0E-A82066121073") Pingable : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> () // type-defs: %5
         |  %7 = apply %3<@opened("1E467EB8-D5C5-11E5-8C0E-A82066121073") Pingable>(%2) : $@convention(witness_method: Pingable) <τ_0_0 where τ_0_0 : Pingable> (@in_guaranteed τ_0_0) -> () // type-defs: %2
         |  destroy_addr %0 : $*Pingable                    // id: %8
         |  %9 = tuple ()                                   // user: %10
         |  return %9 : $()                                 // id: %10
         |}
      """.stripMargin

    val original = silFunction.parse(sil).get.value
    val expected = silFunction.parse(optimizedSil).get.value
    val optimized = CSE.run(original)
    optimized should be(expected)
  }

  ignore should "optimize @cse_open_existential" in {
    val sil =
      """|sil @cse_open_existential : $@convention(thin) (@guaranteed Proto, Bool) -> () {
         |bb0(%0 : $Proto, %1 : $Bool):
         |  %4 = open_existential_ref %0 : $Proto to $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto
         |  %5 = witness_method $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto, #Proto.doThis!1, %4 : $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> ()
         |  %6 = apply %5<@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto>(%4) : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> ()
         |  %7 = struct_extract %1 : $Bool, #Bool._value
         |  cond_br %7, bb1, bb2
         |
         |bb1:
         |  %9 = open_existential_ref %0 : $Proto to $@opened("1B685052-4796-11E6-B7DF-B8E856428C60") Proto
         |  %10 = witness_method $@opened("1B685052-4796-11E6-B7DF-B8E856428C60") Proto, #Proto.doThat!1, %9 : $@opened("1B685052-4796-11E6-B7DF-B8E856428C60") Proto : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> ()
         |  %11 = apply %10<@opened("1B685052-4796-11E6-B7DF-B8E856428C60") Proto>(%9) : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> ()
         |  br bb3
         |
         |bb2:                                              // Preds: bb0
         |  %13 = open_existential_ref %0 : $Proto to $@opened("1B6851A6-4796-11E6-B7DF-B8E856428C60") Proto
         |  %14 = witness_method $@opened("1B6851A6-4796-11E6-B7DF-B8E856428C60") Proto, #Proto.doThis!1, %13 : $@opened("1B6851A6-4796-11E6-B7DF-B8E856428C60") Proto : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> ()
         |  %15 = apply %14<@opened("1B6851A6-4796-11E6-B7DF-B8E856428C60") Proto>(%13) : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> ()
         |  %16 = alloc_stack $@opened("1B6851A6-4796-11E6-B7DF-B8E856428C60") Proto
         |  store %13 to %16 : $*@opened("1B6851A6-4796-11E6-B7DF-B8E856428C60") Proto
         |  // This is to check that result types of instructions are updated by CSE as well.
         |  %17 = load %16 : $*@opened("1B6851A6-4796-11E6-B7DF-B8E856428C60") Proto
         |  strong_release %17 : $@opened("1B6851A6-4796-11E6-B7DF-B8E856428C60") Proto
         |  dealloc_stack %16 : $*@opened("1B6851A6-4796-11E6-B7DF-B8E856428C60") Proto
         |  br bb3
         |
         |bb3:
         |  %20 = tuple ()
         |  return %20 : $()
         |}
      """.stripMargin

    val optimizedSil =
      """|sil @cse_open_existential : $@convention(thin) (@guaranteed Proto, Bool) -> () {
         |bb0(%0 : $Proto, %1 : $Bool):
         |  %2 = open_existential_ref %0 : $Proto to $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto // users: %12, %10, %11, %10, %8, %8, %7, %4, %4, %3
         |  %3 = witness_method $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto, #Proto.doThis!1 : <Self where Self : Proto> (Self) -> () -> (), %2 : $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> () // type-defs: %2; users: %10, %4
         |  %4 = apply %3<@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto>(%2) : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> () // type-defs: %2
         |  %5 = struct_extract %1 : $Bool, #Bool._value    // user: %6
         |  cond_br %5, bb1, bb2                            // id: %6
         |bb1:                                              // Preds: bb0
         |  %7 = witness_method $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto, #Proto.doThat!1 : <Self where Self : Proto> (Self) -> () -> (), %2 : $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> () // type-defs: %2; user: %8
         |  %8 = apply %7<@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto>(%2) : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> () // type-defs: %2
         |  br bb3                                          // id: %9
         |bb2:                                              // Preds: bb0
         |  %10 = apply %3<@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto>(%2) : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> () // type-defs: %2
         |  %11 = alloc_stack $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto // type-defs: %2; users: %13, %12, %15
         |  store %2 to %11 : $*@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto // id: %12
         |  %13 = load %11 : $*@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto // user: %14
         |  strong_release %13 : $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto // id: %14
         |  dealloc_stack %11 : $*@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto // id: %15
         |  br bb3                                          // id: %16
         |bb3:                                              // Preds: bb2 bb1
         |  %17 = tuple ()                                  // user: %18
         |  return %17 : $()                                // id: %18
         |}
      """.stripMargin

    val original = silFunction.parse(sil).get.value
    val expected = silFunction.parse(optimizedSil).get.value
    val optimized = CSE.run(original)
    optimized should be(expected)
  }

  ignore should "not optimize @dont_cse_open_existential_ref" in {
    val sil =
      """|sil @dont_cse_open_existential_ref : $@convention(thin) (@guaranteed Proto & Ping) -> @owned Ping {
         |bb0(%0 : $Proto & Ping):
         |  %4 = open_existential_ref %0 : $Proto & Ping to $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto
         |  %5 = witness_method $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto, #Proto.doThis!1, %4 : $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> ()
         |  %6 = apply %5<@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto>(%4) : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> ()
         |  %9 = open_existential_ref %0 : $Proto & Ping to $@opened("3C038746-BE69-11E7-A5C1-685B35C48C83") Proto & Ping
         |  %10 = upcast %9 : $@opened("3C038746-BE69-11E7-A5C1-685B35C48C83") Proto & Ping to $Ping
         |  %11 = class_method %10 : $Ping, #Ping.ping!1 : (Ping) -> () -> Ping, $@convention(method) (@guaranteed Ping) -> @owned Ping
         |  %12 = apply %11(%10) : $@convention(method) (@guaranteed Ping) -> @owned Ping
         |  return %12 : $Ping
         |}
      """.stripMargin

    val optimizedSil =
      """|sil @dont_cse_open_existential_ref : $@convention(thin) (@guaranteed Ping & Proto) -> @owned Ping {
         |bb0(%0 : $Ping & Proto):
         |  %1 = open_existential_ref %0 : $Ping & Proto to $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto // users: %3, %3, %2
         |  %2 = witness_method $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto, #Proto.doThis!1 : <Self where Self : Proto> (Self) -> () -> (), %1 : $@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> () // type-defs: %1; user: %3
         |  %3 = apply %2<@opened("1B68354A-4796-11E6-B7DF-B8E856428C60") Proto>(%1) : $@convention(witness_method: Proto) <τ_0_0 where τ_0_0 : Proto> (@guaranteed τ_0_0) -> () // type-defs: %1
         |  %4 = open_existential_ref %0 : $Ping & Proto to $@opened("3C038746-BE69-11E7-A5C1-685B35C48C83") Ping & Proto // user: %5
         |  %5 = upcast %4 : $@opened("3C038746-BE69-11E7-A5C1-685B35C48C83") Ping & Proto to $Ping // users: %7, %6
         |  %6 = class_method %5 : $Ping, #Ping.ping!1 : (Ping) -> () -> Ping, $@convention(method) (@guaranteed Ping) -> @owned Ping // user: %7
         |  %7 = apply %6(%5) : $@convention(method) (@guaranteed Ping) -> @owned Ping // user: %8
         |  return %7 : $Ping                               // id: %8
         |}
      """.stripMargin

    val original = silFunction.parse(sil).get.value
    val expected = silFunction.parse(optimizedSil).get.value
    val optimized = CSE.run(original)
    optimized should be(expected)
  }

}
