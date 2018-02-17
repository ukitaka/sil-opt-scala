package me.waft.sil.optimizer.pass

import me.waft.sil.lang.SILFunction

trait Pass[T] {
  def run(t: T): T
}

trait SILFunctionTransform extends Pass[SILFunction] {

}
