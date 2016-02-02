package grind.laws.discipline

import grind.laws.{SafeNodeDecoderLaws, NodeDecoderLaws}
import grind.ops._
import grind.{Element, NodeDecoder}
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws

trait SafeNodeDecoderTests[A] extends Laws {
  def laws: SafeNodeDecoderLaws[A]
  implicit def arbA: Arbitrary[A]

  def safeNodeDecoder: RuleSet = new DefaultRuleSet(
    name = "safeNodeDecoder",
    parent = None,
    "decode first"         -> forAll(laws.decodeFirst _),
    "unsafe decode first"  -> forAll(laws.unsafeDecodeFirst _),
    "lift first"           -> forAll(laws.liftFirst _),
    "lift unsafe first"    -> forAll(laws.liftUnsafeFirst _),
    "decode all"           -> forAll(laws.decodeAll _),
    "unsafe decode all"    -> forAll(laws.unsafeDecodeAll _),
    "lift all"             -> forAll(laws.liftAll _),
    "lift unsafe all"      -> forAll(laws.liftUnsafeAll _)
  )
}

object SafeNodeDecoderTests {
  def cdataEncode(value: String, name: String): Element = {
    val n = s"<$name></$name>".asNode.getFirstChild.asInstanceOf[Element]
    n.setTextContent(value)
    n
  }

  def cdataEncoded[A: NodeDecoder: Arbitrary](f: A => String): SafeNodeDecoderTests[A] =
    SafeNodeDecoderTests((a, name) => cdataEncode(f(a), name))


  def apply[A: NodeDecoder](f: (A, String) => Element)(implicit aa: Arbitrary[A]): SafeNodeDecoderTests[A] = new SafeNodeDecoderTests[A] {
    override val laws = SafeNodeDecoderLaws(f)
    override implicit val arbA = aa
  }
}