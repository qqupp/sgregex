package com.qqupp.sgregexp

import com.qqupp.sgregexp.SGRegExp._
import org.scalatest._

class SGRegExpSpec extends WordSpecLike with Matchers {

  "Empty" should {
    "match an empty sequence" in {
      check[Object](Empty, Seq()) shouldBe true
    }

    "not match a non empty sequence" in {
      check[Object](Empty, Seq(new Object)) shouldBe false
    }
  }

  "Literal" should {
    val aTerm = 'a'
    val bTerm = 'b'
    val literalAExpr = Literal(aTerm)

    "match just the literal" in {
      check(literalAExpr, Seq(aTerm)) shouldBe true
    }

    "not match a different literal" in {
      check(literalAExpr, Seq(bTerm)) shouldBe false
    }

    "not match a sequence with more than one term" in {
      check(literalAExpr, Seq(aTerm, aTerm)) shouldBe false
      check(literalAExpr, Seq(aTerm, bTerm)) shouldBe false
      check(literalAExpr, Seq(bTerm, aTerm)) shouldBe false
      check(literalAExpr, Seq(bTerm, bTerm)) shouldBe false
    }
  }

  "Or" should {
    val zero = 0
    val one = 1
    val two = 2
    val expression1 = Literal(zero)
    val expression2 = Literal(one)
    val orExpr = Or(expression1, expression2)
    val orExpr2 = Or(expression1, Empty)

    "match a sequence containing one expression or the other" in {
      check(orExpr, Seq(one)) shouldBe true
      check(orExpr, Seq(zero)) shouldBe true

      check(orExpr2, Seq()) shouldBe true
      check(orExpr2, Seq(zero)) shouldBe true
    }

    "not match a sequence with non matching terms" in {
      check(orExpr, Seq(two)) shouldBe false
      check(orExpr, Seq(one, two)) shouldBe false
      check(orExpr, Seq(zero, one)) shouldBe false
    }
  }

  "And" should {
    val f = false
    val t = true
    val expression1 = Literal(t)
    val expression2 = Literal(f)
    val andExpr = And(expression1, expression2)

    "match a the first expression and then the second one in sequence" in {
      check(andExpr, Seq(t, f)) shouldBe true
    }

    "not match a sequence that doesn't represent the concatenation of the 2 expressions" in {
      check(andExpr, Seq(t, f, f)) shouldBe false
      check(andExpr, Seq(t, f, t)) shouldBe false
      check(andExpr, Seq(f, t, f)) shouldBe false
      check(andExpr, Seq(t, t, f)) shouldBe false
      check(andExpr, Seq(f, f)) shouldBe false
      check(andExpr, Seq(f, t)) shouldBe false
      check(andExpr, Seq(t, t)) shouldBe false
    }
  }

  "Kleene" should {
    val a = 'a'
    val b = 'b'
    val star = Kleene(Literal(a))

    "match a zero sequence" in {
      check(star, Seq()) shouldBe true
    }

    "match a sequence containing recurring repetition of the pattern" in {
      check(star, Seq(a)) shouldBe true
      check(star, Seq(a, a)) shouldBe true
      check(star, Seq(a, a, a)) shouldBe true
      check(star, Seq(a, a, a, a)) shouldBe true
    }

    "not match a sequence containing a non matchin pattern" in {
      check(star, Seq(b)) shouldBe false
      check(star, Seq(a, b)) shouldBe false
      check(star, Seq(a, b, a)) shouldBe false
      check(star, Seq(a, a, a, b)) shouldBe false
    }
  }

  "check" should {
    "match custom sequence of arbitrary objects" in {
      trait MyDSL
      case object A extends MyDSL
      case object B extends MyDSL
      case class C(i: Int) extends MyDSL
      case class D(b: Boolean) extends MyDSL

      // (A|B)C1(Dfalse*)B
      val regex = And(And(And(Or(Literal(A), Literal(B)), Literal(C(1))),
                          Kleene(Literal(D(false)))),
                      Literal(B))

      val s1 = Seq(A, C(1), D(false), D(false), B)
      val s2 = Seq(B, C(1), B)
      check(regex, s1) shouldBe true
      check(regex, s2) shouldBe true

      // not matching sequences
      val ns1 = Seq(A, C(1), D(false), A, D(false), B)
      val ns2 = Seq(A, D(false), D(false), B)
      val ns3 = Seq(A, C(2), D(false), D(false), B)
      val ns4 = Seq(A, C(1), D(true), D(false), B)

      check(regex, ns1) shouldBe false
      check(regex, ns2) shouldBe false
      check(regex, ns3) shouldBe false
      check(regex, ns4) shouldBe false

    }
  }

}
