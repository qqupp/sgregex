package com.qqupp.sgregexp

import com.qqupp.sgregexp.SGRegExp._

object syntax {

  implicit def anyToLiteral[T](t: T): SGRegExp[T] = Literal(t)
  implicit def sGRegExpOp0[T](t: SGRegExp[T]): SGRegExpOps[T] = new SGRegExpOps(t)
  implicit def sGRegExpOp1[T](t: T): SGRegExpOps[T] = new SGRegExpOps(Literal(t))

  final class SGRegExpOps[T](val t1: SGRegExp[T]) extends AnyVal {
    def and[S >: T](t2: SGRegExp[S]): SGRegExp[S] = And(t1, t2)
    def or[S >: T](t2: SGRegExp[S]): SGRegExp[S] = Or(t1, t2)
    def and[S >: T](t2: S): SGRegExp[S] = And(t1, Literal(t2))
    def or[S >: T](t2: S): SGRegExp[S]  = Or(t1, Literal(t2))
    def star: SGRegExp[T] = Kleene(t1)
  }

}


