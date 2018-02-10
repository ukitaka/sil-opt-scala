# sil-scala

Swift Intermediate Language parser and optimizer written in Scala.

## SIL Document

- [docs/SIL.rst](https://github.com/apple/swift/blob/master/docs/SIL.rst)
- [Swift - Summary of the Grammar](https://developer.apple.com/library/content/documentation/Swift/Conceptual/Swift_Programming_Language/zzSummaryOfTheGrammar.html)

## SIL Instruction Set

### Allocation and Deallocation

- [x] alloc_stack
- [ ] alloc_ref
- [ ] alloc_ref_dynamic
- [x] alloc_box
- [ ] alloc_value_buffer
- [ ] alloc_global
- [ ] dealloc_stack
- [ ] dealloc_box
- [x] project_box
- [ ] dealloc_ref
- [ ] dealloc_partial_ref
- [ ] dealloc_value_buffer
- [ ] project_value_buffer

### Debug Information

- [ ] debug_value
- [ ] debug_value_addr

### Accessing Memory

- [x] load
- [x] store
- [ ] load_borrow
- [ ] end_borrow
- [ ] assign
- [ ] mark_uninitialized
- [ ] mark_function_escape
- [ ] mark_uninitialized_behavior
- [ ] copy_addr
- [ ] destroy_addr
- [ ] index_addr
- [ ] tail_addr
- [ ] index_raw_pointer
- [ ] bind_memory
- [ ] begin_access
- [ ] end_access

### Reference Counting

- [ ] strong_retain
- [x] strong_release
- [ ] set_deallocating
- [ ] strong_retain_unowned
- [ ] unowned_retain
- [ ] unowned_release
- [ ] load_weak
- [ ] store_weak
- [ ] load_unowned
- [ ] store_unowned
- [ ] fix_lifetime
- [ ] mark_dependence
- [ ] strong_pin
- [ ] strong_unpin
- [ ] is_unique
- [ ] is_unique_or_pinned
- [ ] copy_block
- [ ] builtin "unsafeGuaranteed"
- [ ] builtin "unsafeGuaranteedEnd"

### Literals

- [ ] function_ref
- [ ] global_addr
- [ ] global_value
- [x] integer_literal
- [ ] float_literal
- [ ] string_literal

### Dynamic Dispatch

- [ ] class_method
- [ ] objc_method
- [ ] super_method
- [ ] objc_super_method
- [ ] witness_method

### Function Application

- [ ] apply
- [ ] begin_apply
- [ ] abort_apply
- [ ] end_apply
- [ ] partial_apply
- [x] builtin

### Metatypes

- [ ] metatype
- [ ] value_metatype
- [ ] existential_metatype
- [ ] objc_protocol

### Aggregate Types

- [ ] retain_value
- [ ] retain_value_addr
- [ ] unmanaged_retain_value
- [ ] copy_value
- [ ] release_value
- [ ] release_value_addr
- [ ] unmanaged_release_value
- [ ] destroy_value
- [ ] autorelease_value
- [ ] tuple
- [ ] tuple_extract
- [ ] tuple_element_addr
- [ ] destructure_tuple
- [x] struct
- [x] struct_extract
- [ ] struct_element_addr
- [ ] destructure_struct
- [ ] object
- [ ] ref_element_addr
- [ ] ref_tail_addr

### Enums

- [ ] enum
- [ ] unchecked_enum_data
- [ ] init_enum_data_addr
- [ ] inject_enum_addr
- [ ] unchecked_take_enum_data_addr
- [ ] select_enum
- [ ] select_enum_addr

### Protocol and Protocol Composition Types

- [ ] init_existential_addr
- [ ] init_existential_value
- [ ] deinit_existential_addr
- [ ] deinit_existential_value
- [ ] open_existential_addr
- [ ] open_existential_value
- [ ] init_existential_ref
- [ ] open_existential_ref
- [ ] init_existential_metatype
- [ ] open_existential_metatype
- [ ] alloc_existential_box
- [ ] project_existential_box
- [ ] open_existential_box
- [ ] open_existential_box_value
- [ ] dealloc_existential_box

### Blocks

- [ ] project_block_storage
- [ ] init_block_storage_header

### Unchecked Conversions

- [ ] upcast
- [ ] address_to_pointer
- [ ] pointer_to_address
- [ ] unchecked_ref_cast
- [ ] unchecked_ref_cast_addr
- [ ] unchecked_addr_cast
- [ ] unchecked_trivial_bit_cast
- [ ] unchecked_bitwise_cast
- [ ] ref_to_raw_pointer
- [ ] raw_pointer_to_ref
- [ ] ref_to_unowned
- [ ] unowned_to_ref
- [ ] ref_to_unmanaged
- [ ] unmanaged_to_ref
- [ ] convert_function
- [ ] convert_escape_to_noescape
- [ ] thin_function_to_pointer
- [ ] pointer_to_thin_function
- [ ] classify_bridge_object
- [ ] ref_to_bridge_object
- [ ] bridge_object_to_ref
- [ ] bridge_object_to_word
- [ ] thin_to_thick_function
- [ ] thick_to_objc_metatype
- [ ] objc_to_thick_metatype
- [ ] objc_metatype_to_object
- [ ] objc_existential_metatype_to_object

### Checked Conversions

- [ ] unconditional_checked_cast
- [ ] unconditional_checked_cast_addr
- [ ] unconditional_checked_cast_value

### Runtime Failures

- [ ] cond_fail

### Terminators

- [x] unreachable
- [x] return
- [x] throw
- [ ] yield
- [x] unwind
- [x] br
- [x] cond_br
- [ ] switch_value
- [ ] select_value
- [ ] switch_enum
- [ ] switch_enum_addr
- [ ] dynamic_method_br
- [ ] checked_cast_br
- [ ] checked_cast_value_br
- [ ] checked_cast_addr_br
- [ ] try_apply

## SIL Optimization Passes

- [ ] Dump Alias Analysis over all Pairs
- [ ] Array Bounds Check Optimization
- [ ] Access Enforcement Selection
- [ ] Dump Address Parameter Access Summary
- [ ] Access Marker Elimination.
- [ ] SIL Address Lowering
- [ ] Stack Promotion of Box Objects
- [ ] SIL alloc_stack Hoisting
- [ ] Array Count Propagation
- [ ] Array Element Propagation
- [ ] Assume Single-Threaded Environment
- [ ] Print SIL Instruction MemBehavior and ReleaseBehavior Information
- [ ] Print Basic Callee Analysis for Testing
- [ ] View Function CFGs
- [ ] COW Array Optimization
- [ ] Common Subexpression Elimination
- [ ] Print Caller Analysis for Testing
- [ ] Capture Promotion to Eliminate Escaping Boxes
- [ ] Captured Constant Propagation
- [ ] Closure Specialization on Constant Function Arguments
- [ ] Code Sinking
- [ ] Compute Dominance Information for Testing
- [ ] Compute Loop Information for Testing
- [ ] Conditional Branch Forwarding to Fold SIL switch_enum
- [ ] Copy Forwarding to Remove Redundant Copies
- [ ] Print Epilogue retains of Returned Values and Argument releases
- [ ] Print Epilogue retains of Returned Values and Argument releases
- [ ] Redundant Overflow Check Removal
- [x] Dead Code Elimination
- [ ] Dead Argument Elimination via Function Specialization
- [ ] Dead Function Elimination
- [ ] Dead Object Elimination for Classes with Trivial Destruction
- [ ] Definite Initialization for Diagnostics
- [ ] Indirect Call Devirtualization
- [ ] Static Enforcement of Law of Exclusivity
- [ ] Diagnose Unreachable Code
- [ ] Constants Propagation for Diagnostics
- [ ] Eager Specialization via @_specialize
- [ ] Early Code Motion without Release Hoisting
- [ ] Early Inlining Preserving Semantic Functions
- [ ] Emit SIL Diagnostics
- [ ] Dump Escape Analysis Results
- [ ] Print Function Order for Testing
- [ ] Function Signature Optimization
- [ ] ARC Sequence Optimization
- [ ] ARC Loop Optimization
- [ ] Redundant Load Elimination
- [ ] Dead Store Elimination
- [ ] Generic Function Specialization on Static Types
- [ ] SIL Global Optimization
- [ ] Global Property Optimization
- [ ] Guaranteed ARC Optimization
- [ ] Common Subexpression Elimination on High-Level SIL
- [ ] Loop Invariant Code Motion in High-Level SIL
- [ ] Print Induction Variable Information for Testing
- [ ] Record SIL Instruction, Block, and Function Counts as LLVM Statistics
- [ ] Simplify CFG via Jump Threading
- [ ] Let Property Optimization
- [ ] Loop Invariant Code Motion
- [ ] Late Code Motion with Release Hoisting
- [ ] Late Function Inlining
- [ ] Loop Canonicalization
- [ ] Print Loop Information for Testing
- [ ] Print Loop Region Information for Testing
- [ ] View Loop Region CFG
- [ ] Loop Rotation
- [ ] Loop Unrolling
- [ ] Lower Aggregate SIL Instructions to Multiple Scalar Operations
- [ ] Mandatory Inlining of Transparent Functions
- [ ] Memory to SSA Value Conversion to Remove Stack Allocation
- [ ] Print SIL Instruction MemBehavior from Alias Analysis over all Pairs
- [ ] Print Load-Store Location Results Covering all Accesses
- [ ] Merge SIL cond_fail to Eliminate Redundant Overflow Checks
- [ ] Move SIL cond_fail by Hoisting Checks
- [ ] Prune Control Flow at No-Return Calls Using SIL unreachable
- [ ] Function Outlining Optimization
- [ ] Eliminate Ownership Annotation of SIL
- [ ] Print Reference Count Identities
- [ ] Performance Function Inlining
- [ ] Constant Propagation for Performance without Diagnostics
- [ ] Predictable Memory Optimization for Diagnostics
- [ ] SIL release Devirtualization
- [ ] SIL retain Sinking
- [ ] SIL release Hoisting
- [ ] Late SIL release Hoisting Preserving Epilogues
- [ ] SIL Large Loadable type by-address lowering.
- [ ] Remove SIL pin/unpin pairs
- [ ] Remove short-lived immutable temporary copies
- [ ] Print Side-Effect Information for all Functions
- [ ] SIL Cleanup Preparation for IRGen
- [ ] Combine SIL Instructions via Peephole Optimization
- [ ] Generate Debug Information with Source Locations into Textual SIL
- [ ] Link all SIL Referenced within the Module via Deserialization
- [ ] Scalar Replacement of Aggregate Stack Objects
- [ ] Scalar Replacement of Aggregate SIL Block Arguments
- [ ] SIL Block Argument Simplification
- [ ] SIL CFG Simplification
- [ ] Speculative Devirtualization via Guarded Calls
- [ ] Split all Critical Edges in the SIL CFG
- [ ] Split all Critical Edges not from SIL cond_br
- [ ] Stack Promotion of Class Objects
- [ ] Strip Debug Information
- [ ] Loop Specialization for Array Properties
- [ ] SIL retain/release Peephole Removal for Builtin.unsafeGuaranteed
- [ ] Use Pre-Specialized Functions
- [ ] Print Value Ownership Kind for Testing
- [ ] Semantic ARC Optimization
- [ ] Temporary pass for staging in mark_uninitialized changes.
- [ ] Utility pass. Removes all non-term insts from blocks with unreachable terms
- [ ] Utility pass. Serializes the current SILModule
- [ ] sil-bug-reducer Tool Testing by Asserting on a Sentinel Function

