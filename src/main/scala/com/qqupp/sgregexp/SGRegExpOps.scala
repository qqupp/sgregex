package com.qqupp.sgregexp

import com.qqupp.sgregexp.SGRegExp._

object syntax {

  implicit def any2Literal[T](t: T): SGRegExp[T] = Literal(t)

  implicit def SGRegExpBinOp0[T](t1: SGRegExp[T]): SGRegBinOps[T] = new SGRegBinOps(t1)
  implicit def SGRegExpBinOp1[T](t1: T): SGRegBinOps[T] = new SGRegBinOps(Literal(t1))

  final class SGRegBinOps[T](val t1: SGRegExp[T]) extends AnyVal {
    def and[T2 <: T](t2: SGRegExp[T2]): SGRegExp[T] = And(t1, t2)
    def or[T2 <: T](t2: SGRegExp[T2]): SGRegExp[T] = Or(t1, t2)
    def and[S >: T](t2: S): SGRegExp[S] = And(t1, Literal(t2))
    def or[S >: T](t2: S): SGRegExp[S]  = Or(t1, Literal(t2))
  }

  implicit class SGRegExOp0[T](val t: T) extends AnyVal {
    def star: SGRegExp[T] = Kleene(Literal(t))
  }

  implicit class SGRegExOp1[T](val t: SGRegExp[T]) extends AnyVal {
    def star: SGRegExp[T] = Kleene(t)
  }
}


