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

import kantan.xpath.DecodeError
import kantan.xpath.Node
import kantan.xpath.codecs
import kantan.xpath.laws.discipline.arbitrary._
import org.scalacheck.Arbitrary
import org.scalacheck.Cogen
import org.scalacheck.Gen

object NodeDecoderTests {
  implicit val arb: Arbitrary[Node] = arbNode(identity[String])(Arbitrary(Gen.identifier))

  def apply[A: NodeDecoderLaws: Arbitrary: Cogen](implicit al: Arbitrary[LegalNode[A]]): NodeDecoderTests[A] =
    DecoderTests[Option[Node], A, DecodeError, codecs.type]
}
