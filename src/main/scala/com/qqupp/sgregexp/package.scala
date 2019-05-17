package com.qqupp

import com.qqupp.sgregexp.SGRegExp._

package object sgregexp {

  def check[T](pattern: SGRegExp[T], seq: Seq[T]): Boolean = {

    def _match(p: SGRegExp[T], seq: List[T], k: List[T] => Boolean): Boolean =
      (p, seq) match {
        case (Empty, _)             => k(seq)
        case (Literal(t), t1 :: ts) => t == t1 && k(ts)
        case (Literal(_), Nil)      => false
        case (Or(e1, e2), _)        => _match(e1, seq, k) || _match(e2, seq, k)
        case (And(e1, e2), _)       => _match(e1, seq, seq2 => _match(e2, seq2, k))
        case (Kleene(e), _)         => k(seq) || _match(e, seq, seq2 => _match(p, seq2, k))
      }

    _match(pattern, seq.toList, _.isEmpty)
  }
}
