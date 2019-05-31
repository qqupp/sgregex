package com.github.qqupp.sgregexp

sealed abstract class SGRegExp[+T]
object SGRegExp {

  final case object Empty extends SGRegExp[Nothing]
  final case class Literal[T](t: T) extends SGRegExp[T]
  final case class Or[T](e1: SGRegExp[T], e2: SGRegExp[T]) extends SGRegExp[T]
  final case class And[T](e1: SGRegExp[T], e2: SGRegExp[T]) extends SGRegExp[T]
  final case class Kleene[T](e: SGRegExp[T]) extends SGRegExp[T]

}
