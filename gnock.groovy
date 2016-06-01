import static java.util.concurrent.TimeUnit.SECONDS

import groovy.transform.Memoized
import groovy.transform.TailRecursive
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Memoized
def i(def a) {
  a.class in [
    byte, Byte,
    char, Character,
    short, Short,
    int, Integer,
    long, Long,
    BigInteger
  ] && a >= 0
}

@Memoized
def n(def a) {
  def r
  n(a, { r = it })
  r
}

@TailRecursive
def n(def a, def r) {
  if (a in List) {
    if (a.size() == 1) {
      r(a[0])
    } else if (a.size() >= 2) {
      n(a[0], { t ->
        n(a.size() == 2 ? a[1] : a.tail(), { h ->
          r([t, h])
        })
      })
    } else {
      throw new IllegalStateException()
    }
  } else if (i(a)) {
    r((BigInteger) a)
  } else {
    throw new IllegalStateException()
  }
}

@Memoized
def wut(def a) {
  i(a) ? 1 : 0
}

@Memoized
def lus(def a) {
  if (wut(a) == 0) {
    throw new IllegalStateException()
  }
  1 + a
}

@Memoized
def tis(def a) {
  if (wut(a) == 1) {
    throw new IllegalStateException()
  }
  a[0] == a[1] ? 0 : 1
}

@Memoized
def fas(def a) {
  def r
  fas(a, { r = it })
  r
}

@TailRecursive
def fas(def a, def r) {
  if (wut(a) == 1) {
    throw new IllegalStateException()
  }
  def h = a[0]
  if (!i(h)) {
    throw new IllegalStateException()
  }
  def t = a[1]
  if (h == 0) {
    throw new IllegalStateException()
  } else if (h == 1) {
    r(t)
  } else {
    if (i(t)) {
      throw new IllegalStateException()
    }
    if (h == 2) {
      r(t[0])
    } else if (h == 3) {
      r(t[1])
    } else {
      fas([h.intdiv(2), t], { p ->
        fas([2 + h.mod(2), p], { q ->
          r(q)
        })
      })
    }
  }
}

@Memoized
def tar(def a) {
  def r
  tar(a, { r = it})
  r
}

@TailRecursive
def tar(def a, def r) {
  if (wut(a) == 1) {
    throw new IllegalStateException()
  }
  def s = a[0]
  def f = a[1]
  if (wut(f) == 1) {
    throw new IllegalStateException()
  }
  def o = f[0]
  def v = f[1]
  if (wut(o) == 0) {
    tar([s, o], { p ->
      tar([s, v], { q ->
        r([p, q])
      })
    })
  } else {
    if (o == 0) {
      r(fas([v, s]))
    } else if (o == 1) {
      r(v)
    } else if (o == 3) {
      tar([s, v], {
        r(wut(it))
      })
    } else if (o == 4) {
      tar([s, v], {
        r(lus(it))
      })
    } else if (o == 5) {
      tar([s, v], {
        r(tis(it))
      })
    } else {
      if (wut(v) == 1) {
        throw new IllegalStateException()
      }
      def x = v[0]
      def y = v[1]
      if (o == 2) {
        tar([s, x], { p ->
          tar([s, y], { q ->
            tar([p, q], {
              r(it)
            })
          })
        })
      } else if (o == 7) {
        tar(n([s, 2, x, 1, y]), {
          r(it)
        })
      } else if (o == 8) {
        tar(n([s, 7, [[7, [0, 1], x], 0, 1], y]), {
          r(it)
        })
      } else if (o == 9) {
        tar(n([s, 7, y, 2, [0, 1], 0, x]), {
          r(it)
        })
      } else if (o == 10) {
        if (wut(x) == 1) {
          tar([s, y], {
            r(it)
          })
        } else {
          tar(n([s, 8, x[1], 7, [0, 3], y]), {
            r(it)
          })
        }
      } else {
        if (wut(y) == 1) {
          throw new IllegalStateException()
        }
        if (o == 6) {
          tar(n([s, 2, [0, 1], 2, [1, y[0], y[1]], [1, 0], 2, [1, 2, 3], [1, 0], 4, 4, x]), {
            r(it)
          })
        } else {
          throw new IllegalStateException()
        }
      }
    }
  }
}

def err(def cl, def e) {
  try {
    cl()
    false
  } catch (Throwable t) {
    def r = t in e
    if (!r) {
      throw t
    }
    r
  }
}

def ill(def cl) {
  err(cl, IllegalStateException)
}

def inf(def cl) {
  def e = Executors.newSingleThreadExecutor()
  try {
    def f = e.submit(cl)
    SECONDS.sleep(1)
    def r = !f.done
    f.cancel(true)
    err({ f.get() }, CancellationException)
    r
  } finally {
    e.shutdownNow()
  }
}

def l(def s) {
  s = s.readLines().collect {
    def i = it.indexOf(';')
    i != -1 ? it.substring(0, i) : it
  }.join()
  Eval.me(s.replace('\n', ' ')
  .replace(',', '')
  .replace('(', '[')
  .replace(')', ']')
  .replaceAll(/\s+/, ' ')
  .replaceAll(/]\s+]/, ']]')
  .replaceAll(/\[\s+\[/, '[[')
  .replaceAll(/\[\s(\d+)/, '[$1')
  .replaceAll(/(\d+) \]/, '$1]')
  .split()
  .join(', '))
}

def p(def s) {
  def r = []
  def k = new Stack<Integer>()
  s.chars.eachWithIndex { def c, def i ->
    if (c =~ /\[|\(/) {
      k.push(i)
    } else if (c =~ /\]|\)/) {
      def h = k.pop()
      if (k.empty) {
        r << s[h..i]
      }
    }
  }
  r
}

assert n(1) == 1
assert n([1]) == 1
assert n([1, 2]) == [1, 2]
assert n([1, 2, 3]) == [1, [2, 3]]
assert n([1, [2, 3]]) == [1, [2, 3]]
assert n([[1, 2], 3]) == [[1, 2], 3]
assert n([1, 2, 3, 4]) == [1, [2, [3, 4]]]
assert n([1, 2, [3, 4]]) == [1, [2, [3, 4]]]
assert n([1, [2, 3, 4]]) == [1, [2, [3, 4]]]
assert n([1, [2, [3, 4]]]) == [1, [2, [3, 4]]]
assert n([[1, 2], 3, 4]) == [[1, 2], [3, 4]]
assert n([[1, 2], [3, 4]]) == [[1, 2], [3, 4]]
assert n([[1, 2, 3], 4]) == [[1, [2, 3]], 4]
assert n([1, [2, 3], 4]) == [1, [[2, 3], 4]]
assert n([1, 2, 3, 4, 5]) == [1, [2, [3, [4, 5]]]]
assert n([[1, 2], 3, [4, 5]]) == [[1, 2], [3, [4, 5]]]
assert n([1, [2, 3], [4, 5]]) == [1, [[2, 3], [4, 5]]]
assert n([1, 2, 3, 4, 5, 6, 7, 8, 9, 10]) == [1, [2, [3, [4, [5, [6, [7, [8, [9, 10]]]]]]]]]
assert n([[[[[[[[[1, 2], 3], 4], 5], 6], 7], 8], 9], 10]) == [[[[[[[[[1, 2], 3], 4], 5], 6], 7], 8], 9], 10]
assert n([[[1, [[2, [3, 4], 5], [6, 7]], 8], 9], 10]) == [[[1, [[[2, [[3, 4], 5]], [6, 7]], 8]], 9], 10]

assert wut(n([1, 2])) == 0
assert wut(n(1)) == 1
assert lus(n(1)) == 2
assert tis(n([1, 1])) == 0
assert tis(n([1, 2])) == 1
assert fas(n([1, 2])) == 2
assert fas(n([2, 3, 4])) == 3
assert fas(n([3, 4, 5])) == 5

def x = [[4, 5], [6, 14, 15]]
assert fas(n([1, x])) == [[4, 5], [6, [14, 15]]]
assert fas(n([2, x])) == [4, 5]
assert fas(n([3, x])) == [6, [14, 15]]
assert fas(n([4, x])) == 4
assert fas(n([5, x])) == 5
assert fas(n([6, x])) == 6
assert fas(n([7, x])) == [14, 15]
assert ill { fas(n([8, x])) }
assert ill { fas(n([9, x])) }
assert ill { fas(n([10, x])) }
assert ill { fas(n([11, x])) }
assert ill { fas(n([12, x])) }
assert ill { fas(n([13, x])) }
assert fas(n([14, x])) == 14
assert fas(n([15, x])) == 15
assert ill { fas(n([16, x])) }

assert tar(n([2, 0, 1])) == 2
assert tar(n([[2, 3], 0, 1])) == [2, 3]
assert tar(n([[2, 3], 0, 2])) == 2
assert tar(n([[2, 3], 0, 3])) == 3

assert tar(n([1, [1, 2]])) == 2
assert tar(n([1, [1, [2, 3]]])) == [2, 3]

assert tar(n([1, [2, [1, 2], [1, 1, 3, 4]]])) == [3, 4]
assert tar(n([77, [2, [1, 42], [1, 1, 18, 77]]])) == [18, 77]
assert tar(n([1, [2, [4, 0, 1], [1, 4, 0, 1]]])) == 3
assert tar(n([[18, [1, 77]], 2, [0, 1], [0, 3]])) == 77

x = n([[[1, 2], 3], [4, 4]])
assert ill { tar([x, [3, 0]]) }
assert ill { tar([x, [3, 1]]) }
assert ill { tar([x, [3, 2]]) }
assert ill { tar([x, [3, 3]]) }
assert ill { tar([x, [3, 4]]) }
assert tar([x, [3, [0, 1]]]) == 0
assert tar([x, [3, [0, 2]]]) == 0
assert tar([x, [3, [0, 3]]]) == 0
assert tar([x, [3, [0, 4]]]) == 0
assert tar([x, [3, [0, 5]]]) == 1

assert ill { tar([x, [4, 0]]) }
assert ill { tar([x, [4, 1]]) }
assert ill { tar([x, [4, 2]]) }
assert ill { tar([x, [4, 3]]) }
assert ill { tar([x, [4, 4]]) }
assert ill { tar([x, [4, [0, 1]]]) }
assert ill { tar([x, [4, [0, 2]]]) }
assert ill { tar([x, [4, [0, 3]]]) }
assert ill { tar([x, [4, [0, 4]]]) }
assert tar([x, [4, [0, 5]]]) == 4

assert ill { tar([x, [5, 0]]) }
assert ill { tar([x, [5, 1]]) }
assert ill { tar([x, [5, 2]]) }
assert ill { tar([x, [5, 3]]) }
assert ill { tar([x, [5, 4]]) }
assert tar([x, [5, [0, 1]]]) == 1
assert tar([x, [5, [0, 2]]]) == 1
assert tar([x, [5, [0, 3]]]) == 0
assert tar([x, [5, [0, 4]]]) == 1
assert ill { tar([x, [5, [0, 5]]]) }
assert ill { tar([x, [5, [0, 6]]]) }
assert ill { tar([x, [5, [0, 7]]]) }

assert tar(n([1, [6, [1, 0], [1, 2], [1, 3]]])) == 2
assert tar(n([1, [6, [1, 1], [1, 2], [1, 3]]])) == 3

assert tar(n([1, 7, [0, 1], [0, 1]])) == 1
assert tar(n([[1, 2], 7, [0, 1], [0, 1]])) == [1, 2]
assert tar(n([1, 7, [4, 0, 1], [4, 0, 1]])) == 3
assert tar(n([[1, 2], 7, [0, 3], 7, [4, 0, 1], [4, 0, 1]])) == 4
assert tar(n([42, 7, [1, [77, [1, 1, 18]]], [0, 1]])) == [77, [1, [1, 18]]]

assert tar(n([1, [8, [4, 0, 1], [0, 1]]])) == [2, 1]
assert tar(n([1, [8, [4, 0, 1], [4, 0, 2]]])) == 3
assert tar(n([1, [8, [4, 0, 1], [4, 0, 3]]])) == 2
assert tar(n([[1, 2], 8, [[4, 0, 2], [4, 0, 3]], [0, 1]])) == [[2, 3], [1, 2]]
assert tar(n([1, 8, [4, 0, 1], 7, [0, 3], [0, 1]])) == 1

assert tar(n([42, 9, 3, [1, [18, [1, 77]]]])) == 77
assert tar(n([42, 9, 3, [1, [18, [4, 0, 2]]]])) == 19

assert tar(n([1, 10, 18, [4, 0, 1]])) == 2
assert tar(n([1, 10, [18, [0, 1]], [4, 0, 1]])) == 2
assert tar(n([1, 10, [18, [4, 0, 1]], [4, 0, 1]])) == 2
assert tar(n([[1, 2], 10, [0, [4, 0, 2]], [4, 0, 3]])) == 3
assert tar(n([[1, 2], 10, 18, [4, 0, 3]])) == 3
assert tar(n([[1, 2], 10, [18, [4, 0, 3]], [4, 0, 3]])) == 3
assert tar(n(['a' as char, 10, 18, [4, 0, 1]])) == 98
assert tar(n([Long.MAX_VALUE, 10, [18, [4, 0, 1]], [4, 0, 1]])) == 9_223_372_036_854_775_808

assert ill { lus(n([1, 2])) }
assert ill { tis(n(1)) }
assert ill { fas(n(1)) }
assert ill { tar(n(1)) }

assert inf { tar(n([[7, [0, 1], [9, 1, [0, 1]]], [9, 1, [0, 1,]]])) }
assert tar(n(l('''
[
  [
    0
    [
      6
      ; builds a cell like [100 x] where x is the subject's head atom
      ; compares the cell's atoms with = (=[100 x])
      [8 [[1 100] [0 2]] [5 [0 2]]]
      ; if the comparison evaluates to 0 the subject's head atom is returned
      [0 2]
      ; otherwise a new subject is built where the original subject's head atom
      ; is replaced with its incremented value, then the same formula will be
      ; invoked on the new subject resulting in a recursion
      [9 3 [[7 [0 2] [4 0 1]] [0 3]]]
    ]
  ]
  [9 3 [0 1]]
]
'''))) == 100

assert l('[1 2 [3 4] [5 6]]') == [1, 2, [3, 4], [5, 6]]
assert l('(1 (2 3) (4 5 6))') == [1, [2, 3], [4, 5, 6]]

assert p('[1 [2 3]] [4 [5 6]]') == ['[1 [2 3]]', '[4 [5 6]]']

System.in.withReader { r ->
  System.out.withPrintWriter { o ->
    p(r.text).each {
      o.println(tar(n(l(it))))
    }
  }
}

System.exit(0)
