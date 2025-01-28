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

package kantan.xpath.cats

import cats.Eq
import cats.data.EitherT
import cats.instances.all._
import cats.laws.discipline.MonadErrorTests
import cats.laws.discipline.SemigroupKTests
import cats.laws.discipline.SemigroupalTests.Isomorphisms
import kantan.codecs.laws.discipline.DisciplineSuite
import kantan.xpath.DecodeError
import kantan.xpath.Node
import kantan.xpath.NodeDecoder
import kantan.xpath.cats.arbitrary._
import kantan.xpath.cats.equality._
import org.scalacheck.Arbitrary

class DecoderTests extends DisciplineSuite {

  implicit val arb: Arbitrary[Node] = arbNode[Int](_.toString)

  // For some reason, these are not derived automatically. I *think* it's to do with GroupDecoder being a type alias
  // for a type with many holes, but this is slightly beyond me.
  implicit val eqGroupEitherT: Eq[EitherT[NodeDecoder, DecodeError, Int]] = EitherT.catsDataEqForEitherT
  implicit val groupIso: Isomorphisms[NodeDecoder]                        = Isomorphisms.invariant

  checkAll("NodeDecoder", SemigroupKTests[NodeDecoder].semigroupK[Int])
  checkAll("NodeDecoder", MonadErrorTests[NodeDecoder, DecodeError].monadError[Int, Int, Int])

}
