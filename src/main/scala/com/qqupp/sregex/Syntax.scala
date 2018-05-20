package com.qqupp.sregex

object Syntax {

  sealed trait SGRegex[+T]

  case object Empty extends SGRegex[Nothing]
  case class Literal[T](t: T) extends SGRegex[T]
  case class Or[T](e1: SGRegex[T], e2: SGRegex[T]) extends SGRegex[T]
  case class And[T](e1: SGRegex[T], e2: SGRegex[T]) extends SGRegex[T]
  case class Kleene[T](e: SGRegex[T]) extends SGRegex[T]

}
