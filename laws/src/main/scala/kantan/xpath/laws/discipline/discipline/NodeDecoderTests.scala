package kantan.xpath.laws.discipline.discipline

import kantan.xpath.laws.discipline.NodeDecoderLaws
import kantan.xpath.ops
import ops._
import kantan.xpath.{Element, NodeDecoder}
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.typelevel.discipline.Laws

trait NodeDecoderTests[A] extends Laws {
  def laws: NodeDecoderLaws[A]
  implicit def arbA: Arbitrary[A]

  def nodeDecoder: RuleSet = new DefaultRuleSet(
    name = "nodeDecoder",
    parent = None,
    "decode first"         -> forAll(laws.decodeFirst _),
    "lift first"           -> forAll(laws.liftFirst _),
    "decode all"           -> forAll(laws.decodeAll _),
    "lift all"             -> forAll(laws.liftAll _),
    "lift every"           -> forAll(laws.liftEvery _)
  )
}

object NodeDecoderTests {
  def cdataEncode(value: String, name: String): Element = {
    val n = s"<$name></$name>".asUnsafeNode.getFirstChild.asInstanceOf[Element]
    n.setTextContent(value)
    n
  }

  def cdataEncoded[A: NodeDecoder: Arbitrary](f: A => String): NodeDecoderTests[A] =
    NodeDecoderTests((a, name) => cdataEncode(f(a), name))


  def apply[A: NodeDecoder](f: (A, String) => Element)(implicit aa: Arbitrary[A]): NodeDecoderTests[A] = new NodeDecoderTests[A] {
    override val laws = NodeDecoderLaws(f)
    override implicit val arbA = aa
  }
}