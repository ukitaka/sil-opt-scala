package me.waft.sil.optimizer.meta

import scala.collection.generic.CanBuildFrom
import scala.reflect.ClassTag
import scalax.collection.GraphPredef._
import scalax.collection._
import scalax.collection.config.CoreConfig
import scalax.collection.generic.ImmutableGraphCompanion
import scalax.collection.immutable.{AdjacencyListGraph, Graph => ImmutableGraph}
import scalax.collection.mutable.ArraySet

abstract class SILGraph[N, E[X] <: EdgeLikeIn[X]]
( iniNodes: Traversable[N]    = Nil, iniEdges: Traversable[E[N]] = Nil)
( implicit override val edgeT: ClassTag[E[N]])
  extends ImmutableGraph[N,E]
    with    AdjacencyListGraph[N,E,SILGraph]
    with    GraphTraversalImpl[N,E]
    with    Serializable {

  val graphCompanion: ImmutableGraphCompanion[SILGraph] = SILGraph

  protected type Config = CoreConfig

  override final val config = CoreConfig()

  @inline final protected def newNodeSet: NodeSetT = new NodeSet
  @transient private[this] val _nodes: NodeSetT = newNodeSet
  @inline override final def nodes = _nodes

  @transient private[this] val _edges: EdgeSetT = new EdgeSet
  @inline override final def edges = _edges

  initialize(iniNodes, iniEdges)

  @inline final override def empty = SILGraph.empty[N,E]
  @inline final override def clone = SILGraph.from [N,E](nodes.toOuter,
    edges.toOuter)
  @inline final override def copy(nodes: Traversable[N],
                                  edges: Traversable[E[N]])= SILGraph.from[N,E](nodes, edges)

  final protected class NodeBase(value: N, hints: ArraySet.Hints)
    extends InnerNodeImpl(value, hints)
      with    InnerNode // inner class of  extension trait
      with    InnerNodeTraversalImpl

  type NodeT = NodeBase

  @inline final protected def newNodeWithHints(n: N, h: ArraySet.Hints) = new NodeT(n, h)
}

object SILGraph extends ImmutableGraphCompanion[SILGraph] { self =>
  def empty[N, E[X] <: EdgeLikeIn[X]](implicit edgeT: ClassTag[E[N]], config: Config) =
    new SILGraph[N,E] {
      override val graphCompanion = self
    }

  override def from [N, E[X] <: EdgeLikeIn[X]](nodes: Traversable[N] = Nil,
                                               edges: Traversable[E[N]])
                                              (implicit edgeT: ClassTag[E[N]],
                                               config: Config) =
    new SILGraph[N,E](nodes, edges) {
      override val graphCompanion = self
    }

  implicit def canBuildFrom[N, E[X] <: EdgeLikeIn[X]](implicit edgeT: ClassTag[E[N]],
                                                      config: Config): CanBuildFrom[Coll, InParam[N,E], SILGraph[N,E]] =
    new GraphCanBuildFrom[N,E]
}

