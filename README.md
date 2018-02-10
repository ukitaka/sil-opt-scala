# sil-scala

## SIL Document

[docs/SIL.rst](https://github.com/apple/swift/blob/master/docs/SIL.rst)

## Parser

[lib/ParseSIL/ParseSIL.cpp](https://github.com/apple/swift/blob/master/lib/ParseSIL/ParseSIL.cpp)

## Check if emitted SIL is valid

Uses `sil-opt` command.

```
sil-opt stage.sil
```

You have to build swift compiler at first to use `sil-opt` command.

## Passes

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
- [ ] Dead Code Elimination
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
