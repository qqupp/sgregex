package com.github.qqupp.sgregexp

import org.scalatest._
import SGRegExp._
import syntax._

class SyntaxSpec extends WordSpecLike with Matchers {

  "A SGRegExp syntax" should {
    "convert any literal automatically" in {
      val sgRegExp: SGRegExp[Char] = 'a'
      sgRegExp shouldBe Literal('a')
    }

    "support concatenation" in {
      val sgRegExp: SGRegExp[String] = "a" and "b"
      sgRegExp shouldBe And(Literal("a"), Literal("b"))
    }

    "support left concatenation" in {
      val left: SGRegExp[Int] = 1
      val sgRegExp: SGRegExp[Int] = left and 2
      sgRegExp shouldBe And(Literal(1), Literal(2))
    }

    "support right concatenation" in {
      val rigth: SGRegExp[Int] = 2
      val sgRegExp: SGRegExp[Int] = 1 and rigth
      sgRegExp shouldBe And(Literal(1), Literal(2))
    }

    "support multiple concatenation left associative" in {
      val sgRegExp: SGRegExp[Int] = 1 and 2 and 3 and 4
      sgRegExp shouldBe And(And(And(Literal(1), Literal(2)), Literal(3)),
                            Literal(4))
    }

    "support concatenation between 2 sgRegExp with the same type" in {
      val s1: SGRegExp[Int] = 1
      val s2: SGRegExp[Int] = 2
      val sgRegExp = s1 and s2
      sgRegExp shouldBe And(Literal(1), Literal(2))
    }

    "support concatenation and alternation ADT considering all the variances for types" in {
      sealed trait ADT
      case class A() extends ADT
      case class B() extends ADT

      val expectedAnd: SGRegExp[ADT] = And(Literal(A()), Literal(B()))
      val expectedOr: SGRegExp[ADT] = Or(Literal(A()), Literal(B()))

      val aA: A = A()
      val aAdt: ADT = A()
      val aExpA: SGRegExp[A] = Literal(A())
      val aExpADT: SGRegExp[ADT] = Literal(A())

      val bB: B = B()
      val bAdt: ADT = B()
      val bExpB: SGRegExp[B] = Literal(B())
      val bExpADT: SGRegExp[ADT] = Literal(B())

      val exp1: SGRegExp[ADT] = aA and bB
      val exp2: SGRegExp[ADT] = aA and bAdt
      val exp3: SGRegExp[ADT] = aA and bExpB
      val exp4: SGRegExp[ADT] = aA and bExpADT

      val exp5: SGRegExp[ADT] = aAdt and bB
      val exp6: SGRegExp[ADT] = aAdt and bAdt
      val exp7: SGRegExp[ADT] = aAdt and bExpB
      val exp8: SGRegExp[ADT] = aAdt and bExpADT

      val exp9: SGRegExp[ADT] = aExpA and bB
      val exp10: SGRegExp[ADT] = aExpA and bAdt
      val exp11: SGRegExp[ADT] = aExpA and bExpB
      val exp12: SGRegExp[ADT] = aExpA and bExpADT

      val exp13: SGRegExp[ADT] = aExpADT and bB
      val exp14: SGRegExp[ADT] = aExpADT and bAdt
      val exp15: SGRegExp[ADT] = aExpADT and bExpB
      val exp16: SGRegExp[ADT] = aExpADT and bExpADT

      List(exp1,
           exp2,
           exp3,
           exp4,
           exp5,
           exp6,
           exp7,
           exp8,
           exp9,
           exp10,
           exp11,
           exp12,
           exp13,
           exp14,
           exp15,
           exp16)
        .foreach(expr => expr shouldBe expectedAnd)

      val exp17: SGRegExp[ADT] = aA or bB
      val exp18: SGRegExp[ADT] = aA or bAdt
      val exp19: SGRegExp[ADT] = aA or bExpB
      val exp20: SGRegExp[ADT] = aA or bExpADT

      val exp21: SGRegExp[ADT] = aAdt or bB
      val exp22: SGRegExp[ADT] = aAdt or bAdt
      val exp23: SGRegExp[ADT] = aAdt or bExpB
      val exp24: SGRegExp[ADT] = aAdt or bExpADT

      val exp25: SGRegExp[ADT] = aExpA or bB
      val exp26: SGRegExp[ADT] = aExpA or bAdt
      val exp27: SGRegExp[ADT] = aExpA or bExpB
      val exp28: SGRegExp[ADT] = aExpA or bExpADT

      val exp29: SGRegExp[ADT] = aExpADT or bB
      val exp30: SGRegExp[ADT] = aExpADT or bAdt
      val exp31: SGRegExp[ADT] = aExpADT or bExpB
      val exp32: SGRegExp[ADT] = aExpADT or bExpADT

      List(exp17,
           exp18,
           exp19,
           exp20,
           exp21,
           exp22,
           exp23,
           exp24,
           exp25,
           exp26,
           exp27,
           exp28,
           exp29,
           exp30,
           exp31,
           exp32)
        .foreach(expr => expr shouldBe expectedOr)
    }

    "support alternation" in {
      val sgRegExp: SGRegExp[String] = "a" or "b"
      sgRegExp shouldBe Or(Literal("a"), Literal("b"))
    }

    "support left alternation" in {
      val left: SGRegExp[Int] = 1
      val sgRegExp: SGRegExp[Int] = left or 2
      sgRegExp shouldBe Or(Literal(1), Literal(2))
    }

    "support right alternation" in {
      val rigth: SGRegExp[Int] = 2
      val sgRegExp: SGRegExp[Int] = 1 or rigth
      sgRegExp shouldBe Or(Literal(1), Literal(2))
    }

    "support multiple alternation left associative" in {
      val sgRegExp: SGRegExp[Int] = 1 or 2 or 3 or 4
      sgRegExp shouldBe Or(Or(Or(Literal(1), Literal(2)), Literal(3)),
                           Literal(4))
    }

    "support alternation between 2 sgRegExp with the same type" in {
      val s1: SGRegExp[Int] = 1
      val s2: SGRegExp[Int] = 2
      val sgRegExp = s1 or s2
      sgRegExp shouldBe Or(Literal(1), Literal(2))
    }

    "support repetition" in {
      val sgRegExp: SGRegExp[Int] = 1.star
      sgRegExp shouldBe Kleene(Literal(1))
    }

    "support repetition for SGRegExp type" in {
      val expr: SGRegExp[Int] = 1
      val sgRegExp: SGRegExp[Int] = expr.star
      sgRegExp shouldBe Kleene(Literal(1))
    }

    "simplify custom dsl expressions" in {
      sealed trait MyDSL extends Product with Serializable
      final case object A extends MyDSL
      final case object B extends MyDSL
      final case class C(i: Int) extends MyDSL
      final case class D(b: Boolean) extends MyDSL

      val expectedExpression: SGRegExp[MyDSL] =
        And(
          And(
            And(
              Or(
                Literal(A),
                Literal(B)
              ),
              Literal(C(1))
            ),
            Kleene(Literal(D(false)))
          ),
          Literal(B)
        )

      val syntaxExpression: SGRegExp[MyDSL] =
        (A or B) and C(1) and D(false).star and B

      syntaxExpression shouldBe expectedExpression

    }
  }
}
