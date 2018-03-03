package me.waft.sil.optimizer.pass

import me.waft.sil.lang.SILFunction

case class Mem2Reg(function: SILFunction) {

}

object Mem2Reg {
  def run(function: SILFunction): SILFunction = ???
}
