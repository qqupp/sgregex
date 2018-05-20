package com.qqupp.sgregexp

object Syntax {

  sealed trait SGRegExp[+T]

  case object Empty extends SGRegExp[Nothing]
  case class Literal[T](t: T) extends SGRegExp[T]
  case class Or[T](e1: SGRegExp[T], e2: SGRegExp[T]) extends SGRegExp[T]
  case class And[T](e1: SGRegExp[T], e2: SGRegExp[T]) extends SGRegExp[T]
  case class Kleene[T](e: SGRegExp[T]) extends SGRegExp[T]

}
