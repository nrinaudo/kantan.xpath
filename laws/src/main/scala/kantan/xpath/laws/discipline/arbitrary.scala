/*
 * Copyright 2016 Nicolas Rinaudo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kantan.xpath.laws.discipline

import kantan.codecs.Result
import kantan.codecs.laws._
import kantan.codecs.laws.CodecValue.{IllegalValue, LegalValue}
import kantan.xpath._
import kantan.xpath.ops._
import org.scalacheck._
import org.scalacheck.Arbitrary.{arbitrary => arb}
import org.scalacheck.Gen._

object arbitrary extends ArbitraryInstances

trait ArbitraryInstances extends kantan.codecs.laws.discipline.ArbitraryInstances
                                 with kantan.xpath.laws.discipline.ArbitraryArities {
  // - Arbitrary errors ------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit val arbDecodeError: Arbitrary[DecodeError] =
    Arbitrary(oneOf(const(DecodeError.NotFound), const(DecodeError.TypeError(new Exception))))
  implicit val arbParseError: Arbitrary[ParseError] =
    Arbitrary(oneOf(const(ParseError.SyntaxError(new Exception())), const(ParseError.IOError(new Exception()))))
  implicit val arbXPathError: Arbitrary[ReadError] =
    Arbitrary(oneOf(arb[DecodeError], arb[ParseError]))



  // - Arbitrary results -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  implicit def arbDecodeResult[A: Arbitrary]: Arbitrary[DecodeResult[A]] =
    Arbitrary(oneOf(arb[Result.Failure[DecodeError]], arb[Result.Success[A]]))
  implicit def arbReadResult[A: Arbitrary]: Arbitrary[ReadResult[A]] =
    Arbitrary(oneOf(arb[Result.Failure[ReadError]], arb[Result.Success[A]]))



  // - Arbitrary values ------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  private def asCDataNode(value: String): Node = {
    val n = s"<element></element>".asUnsafeNode.getFirstChild.asInstanceOf[Element]
    n.setTextContent(value)
    n
  }

  implicit val arbLegalXml: Arbitrary[LegalValue[String, Node]] = Arbitrary(for {
    element ← Gen.identifier
    content ← Gen.identifier
  } yield {
    val n = s"<$element>$content</$element>"
    LegalValue(n, n.asUnsafeNode)
  })

  implicit val arbIllegalXml: Arbitrary[IllegalValue[String, Node]] =
    Arbitrary(Gen.alphaStr.suchThat(_.asNode.isFailure).map(IllegalValue.apply))


  implicit def arbLegalFoundNode[A](implicit la: Arbitrary[LegalString[A]]): Arbitrary[LegalValue[Node, A]] =
      Arbitrary(la.arbitrary.map(_.mapEncoded(asCDataNode)))
  implicit def arbLegalNode[A](implicit la: Arbitrary[LegalValue[Node, A]]): Arbitrary[LegalNode[A]] =
    Arbitrary(la.arbitrary.map(_.mapEncoded(Option.apply)))

  implicit def arbIllegalFoundNode[A](implicit ia: Arbitrary[IllegalString[A]]): Arbitrary[IllegalValue[Node, A]] =
      Arbitrary(ia.arbitrary.map(_.mapEncoded(asCDataNode)))
  implicit def arbIllegalNode[A](implicit ia: Arbitrary[IllegalValue[Node, A]]): Arbitrary[IllegalNode[A]] =
    Arbitrary(ia.arbitrary.map(_.mapEncoded(Option.apply)))

  implicit def arbLegalNodeOpt[A](implicit la: Arbitrary[LegalString[A]]): Arbitrary[LegalNode[Option[A]]] =
    Arbitrary(Gen.oneOf(
      la.arbitrary.map(_.mapEncoded(e ⇒ Option(asCDataNode(e))).mapDecoded(Option.apply)),
      Gen.const(CodecValue.LegalValue[Option[Node], Option[A]](Option.empty, Option.empty))
    ))

  implicit def arbIllegalNodeOpt[A](implicit la: Arbitrary[IllegalString[A]]): Arbitrary[IllegalNode[Option[A]]] =
    Arbitrary(la.arbitrary.map(_.mapEncoded(e ⇒ Option(asCDataNode(e))).mapDecoded(Option.apply)))



  // - Misc. arbitraries -----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  def arbNode[A: Arbitrary](f: A ⇒ String): Arbitrary[Node] =
    Arbitrary(Arbitrary.arbitrary[A].map(a ⇒ s"<root>${f(a)}</root>".asUnsafeNode))

  implicit def arbNodeDecoder[A: Arbitrary]: Arbitrary[NodeDecoder[A]] =
    Arbitrary(arb[Option[Node] ⇒ DecodeResult[A]].map(f ⇒ NodeDecoder(f)))

  /*
  implicit def arbTuple1[A: Arbitrary]: Arbitrary[Tuple1[A]] =
    Arbitrary(arb[A].map(a ⇒ Tuple1(a)))
    */

  implicit def arbQuery[A: Arbitrary]: Arbitrary[Query[A]] =
    Arbitrary(implicitly[Arbitrary[Node ⇒ A]].arbitrary.map(f ⇒ Query(f)))
}
