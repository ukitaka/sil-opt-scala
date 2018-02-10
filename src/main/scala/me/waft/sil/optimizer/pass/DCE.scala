package me.waft.sil.optimizer.pass

import me.waft.sil.lang.SILBasicBlock

object DCE extends Pass {
  def eliminateDeadCode(bb: SILBasicBlock): SILBasicBlock = {
    bb
  }
}
