package me.waft.sil.optimizer.pass

import me.waft.sil.lang.SILFunction

object CSE extends SILFunctionTransform {
  def run(function: SILFunction): SILFunction = function
}
