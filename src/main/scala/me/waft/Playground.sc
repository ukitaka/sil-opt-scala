object LengauerTarjan {
  // Implement these three yourself
  def successors(v: Int): Iterable[Int] = ???

  def predecessors(v: Int): Iterable[Int] = ???

  def numNodes: Int = ???

  // Lifted from "Modern Compiler Implementation in Java", 2nd ed. chapter 19.2
  def computeDominatorTree(): Array[Int] = {
    var N = 0

    val bucket = Array.fill(numNodes)(Set.empty[Int])
    val dfnum = Array.fill(numNodes)(0)
    val vertex = Array.fill(numNodes)(-1)
    val parent = Array.fill(numNodes)(-1)
    val semi = Array.fill(numNodes)(-1)
    val ancestor = Array.fill(numNodes)(-1)
    val idom = Array.fill(numNodes)(-1)
    val samedom = Array.fill(numNodes)(-1)
    val best = Array.fill(numNodes)(-1)

    def dfs(): Unit = {
      var stack = (-1, 0) :: Nil
      while (stack.nonEmpty) {
        val (p, n) = stack.head
        stack = stack.tail
        if (dfnum(n) == 0) {
          dfnum(n) = N
          vertex(N) = n
          parent(n) = p
          N += 1

          for (w <- successors(n)) {
            stack = (n, w) :: stack
          }
        }
      }
    }

    def ancestorWithLowestSemi(v: Int): Int = {
      val a = ancestor(v)
      if (ancestor(a) >= 0) {
        val b = ancestorWithLowestSemi(a)
        ancestor(v) = ancestor(a)
        if (dfnum(semi(b)) < dfnum(semi(best(v)))) {
          best(v) = b
        }
      }
      best(v)
    }

    def link(p: Int, n: Int): Unit = {
      ancestor(n) = p
      best(n) = n
    }

    dfs()

    for (i <- (N - 1) until 0 by -1) {
      val n = vertex(i); val p = parent(n); var s = p

      for (v <- predecessors(n)) {
        val sPrime = if (dfnum(v) <= dfnum(n)) {
          v
        } else {
          semi(ancestorWithLowestSemi(v))
        }
        if (dfnum(sPrime) < dfnum(s)) {
          s = sPrime
        }
      }

      semi(n) = s
      bucket(s) = bucket(s) + n
      link(p, n)

      for (v <- bucket(p)) {
        val y = ancestorWithLowestSemi(v)
        if (semi(y) == semi(v)) {
          idom(v) = p
        } else {
          samedom(v) = y
        }
      }
      bucket(p) = Set.empty
    }

    for (i <- 1 to N) {
      val n = vertex(i)
      if (samedom(n) >= 0) {
        idom(n) = idom(samedom(n))
      }
    }
    idom
  }
}