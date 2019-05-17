# sgregexp
Scala Generic Regular Expression Library

### Overview

Normal [regular expressions](https://en.wikipedia.org/wiki/Regular_expression)
allow you to validate sequences of characters.

sgregexp allows you to validate
any `Seq[T]` not only characters, the expression can be composed using literals
of any type you want.

### Examples

##### An expression to validate sequences of String
`stringRegExp` represent a sequence of exactly 2 strings with the first being
either foo or bar and the second baz, any other sequence won't match.
```scala
val stringRegExp = And(Or(Literal("foo"), Literal("bar")), Literal("baz"))
check(stringRegExp,Seq("foo", "baz"))
```

##### An expression to validate sequences of a custom DSL

Given a custom DSL
```scala
trait MyDSL
case object A extends MyDSL
case object B extends MyDSL
case class C(i: Int) extends MyDSL
case class D(b: Boolean) extends MyDSL
```

`myDSLRegExp` represents any `Seq[MyDSL]` starting with either A or B followed
by exactly one C(1) and followed by any number including zero of D(false) and
ending with a B
```scala
val myDSLRegExp =
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
```

Or you can import the syntax module for concise syntax
```scala
import com.qqupp.sgregexp.syntax._
val myDSLRegExp : SGRegExp[MyDSL] = (A or B) and C(1) and D(false).star and B

```

```scala
val sequenceToValidate = Seq(A, C(1), D(false), D(false), B)
check(myDSLRegExp, sequenceToValidate)
```

the check method returns true if the sequence to validate respect the given regular expression. 

### Copyright and License

All code is available to you under the MIT license, available at http://opensource.org/licenses/mit-license.php and also in the [LICENSE](LICENSE) file