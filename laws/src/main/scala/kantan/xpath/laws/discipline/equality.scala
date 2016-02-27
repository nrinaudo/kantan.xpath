package kantan.xpath.laws.discipline

import kantan.xpath.ops._
import kantan.xpath.{Node, NodeDecoder, EvaluationResult}
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.{arbitrary => arb}

object equality {
  def nodeDecoder[A: Arbitrary](c1: NodeDecoder[A], c2: NodeDecoder[A])(e: A ⇒ String)(f: (EvaluationResult[A], EvaluationResult[A]) ⇒ Boolean): Boolean = {
    implicit val arbNode: Arbitrary[Node] = Arbitrary(arb[A].map(a ⇒ s"<root>$a</root>".asUnsafeNode))
    kantan.codecs.laws.discipline.equality.eq(c1.decode, c2.decode)(f)
  }
}
