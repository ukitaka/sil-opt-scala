package me.waft.sil.optimizer.analysis

import me.waft.sil.lang.SILBasicBlock

class CDG {

}

object CDG {
  def dominanceFrontier(cfg: CFG): Map[SILBasicBlock, Set[SILBasicBlock]] = ???
  def from(cfg: CFG): CDG = ???
}