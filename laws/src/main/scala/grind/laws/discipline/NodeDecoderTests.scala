package grind.laws.discipline

import grind.{Node, NodeDecoder}
import grind.laws.NodeDecoderLaws
import grind.ops._
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary
import org.typelevel.discipline.Laws

trait NodeDecoderTests[A] extends Laws {
  def laws: NodeDecoderLaws[A]
  implicit def arbA: Arbitrary[A]

  def nodeDecoder: RuleSet = new DefaultRuleSet(
    name = "nodeDecoder",
    parent = None,
    "decode first"         -> forAll(laws.decodeFirst _),
    "unsafe decode first"  -> forAll(laws.unsafeDecodeFirst _),
    "lift first"           -> forAll(laws.liftFirst _),
    "lift unsafe first"    -> forAll(laws.liftUnsafeFirst _)
  )
}

object NodeDecoderTests {
  def cdataEncoded[A: NodeDecoder: Arbitrary](f: A => String): NodeDecoderTests[A] =
    NodeDecoderTests { (a, name) => s"<root><$name>${f(a)}</$name></root>".asNode }

  def apply[A: NodeDecoder](f: (A, String) => Node)(implicit aa: Arbitrary[A]): NodeDecoderTests[A] = new NodeDecoderTests[A] {
    override val laws = NodeDecoderLaws(f)
    override implicit val arbA = aa
  }
}