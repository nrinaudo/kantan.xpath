package grind.laws.discipline

import grind._
import grind.laws.{IllegalValue, NodeDecoderLaws}
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._

trait NodeDecoderTests[A] extends SafeNodeDecoderTests[A] {
  def laws: NodeDecoderLaws[A]
  implicit def arbIllegalA: Arbitrary[IllegalValue[A]]

  def nodeDecoder: RuleSet = new RuleSet {
      def name = "nodeDecoder"
      def bases = Nil
      def parents = Seq(safeNodeDecoder)
      def props = Seq(
        "decode first failure" -> forAll(laws.decodeFirstFail _),
        "unsafe decode first failure" -> forAll(laws.unsafeDecodeFirstFail _),
        "lift first failure" -> forAll(laws.liftFirstFail _),
        "lift unsafe first failure" -> forAll(laws.liftUnsafeFirstFail _),
        "decode all failure" -> forAll(laws.decodeAllFail _),
        "unsafe decode all failure" -> forAll(laws.unsafeDecodeAllFail _),
        "lift all failure" -> forAll(laws.liftAllFail _),
        "lift unsafe all failure" -> forAll(laws.liftUnsafeAllFail _)
      )
    }
}

object NodeDecoderTests {
  def cdataEncoded[A: NodeDecoder: Arbitrary](f: A => String)(implicit ia: Arbitrary[IllegalValue[A]]): NodeDecoderTests[A] =
    NodeDecoderTests[A]((a, name) => SafeNodeDecoderTests.cdataEncode(f(a), name))((s, name) => SafeNodeDecoderTests.cdataEncode(s, name) )

  def apply[A: NodeDecoder](f: (A, String) => Element)(g: (String, String) => Element)(implicit aa: Arbitrary[A], ia: Arbitrary[IllegalValue[A]]): NodeDecoderTests[A] = new NodeDecoderTests[A] {
    override val laws = NodeDecoderLaws(f)(g)
    override implicit val arbA = aa
    override implicit def arbIllegalA = ia
  }
}