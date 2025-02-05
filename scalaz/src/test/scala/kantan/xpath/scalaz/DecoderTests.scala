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

package kantan.xpath.scalaz

import kantan.codecs.scalaz.laws.discipline.ScalazDisciplineSuite
import kantan.xpath.DecodeError
import kantan.xpath.Node
import kantan.xpath.NodeDecoder
import kantan.xpath.scalaz.arbitrary._
import kantan.xpath.scalaz.equality._
import org.scalacheck.Arbitrary
import scalaz.Scalaz._
import scalaz.scalacheck.ScalazProperties._

class DecoderTests extends ScalazDisciplineSuite {

  implicit val arb: Arbitrary[Node] = arbNode[Int](_.toString)

  checkAll("NodeDecoder", plus.laws[NodeDecoder])
  checkAll("NodeDecoder", monadError.laws[NodeDecoder, DecodeError])

}
