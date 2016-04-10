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

import kantan.codecs.laws.discipline.DecoderTests
import kantan.xpath._
import kantan.xpath.laws.discipline.arbitrary._
import org.scalacheck.Arbitrary

object NodeDecoderTests {
  def apply[A](implicit la: NodeDecoderLaws[A], al: Arbitrary[LegalNode[A]]): NodeDecoderTests[A] =
    DecoderTests[Node, A, DecodeError, codecs.type]
}
