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
