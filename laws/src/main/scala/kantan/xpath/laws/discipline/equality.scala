package kantan.xpath.laws.discipline

import kantan.xpath.ops._
import kantan.xpath.{Node, NodeDecoder, EvaluationResult}
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.{arbitrary => arb}

object equality {
  def eq[A, B: Arbitrary](a1: B ⇒ A, a2: B ⇒ A)(f: (A, A) ⇒ Boolean): Boolean = {
    val samples = List.fill(100)(arb[B].sample).collect {
      case Some(a) ⇒ a
      case None    ⇒ sys.error("Could not generate arbitrary values to compare two functions")
    }
    samples.forall(b ⇒ f(a1(b), a2(b)))
  }

  def nodeDecoder[A: Arbitrary](c1: NodeDecoder[A], c2: NodeDecoder[A])(e: A ⇒ String)(f: (EvaluationResult[A], EvaluationResult[A]) ⇒ Boolean): Boolean = {
    implicit val arbNode: Arbitrary[Node] = Arbitrary(arb[A].map(a ⇒ s"<root>$a</root>".asUnsafeNode))
    eq(c1.decode, c2.decode)(f)
  }
}
