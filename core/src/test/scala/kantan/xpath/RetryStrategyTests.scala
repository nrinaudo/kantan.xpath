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

package kantan.xpath

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class RetryStrategyTests extends FunSuite with GeneratorDrivenPropertyChecks {
  implicit val arbInt: Arbitrary[Int] = Arbitrary(Gen.choose(0, 100))
  implicit val arbLong: Arbitrary[Long] = Arbitrary(Gen.choose(0L, 24 * 60 * 60 * 1000L))

  test("RetryStrategy.NoDelay should always have a delay of 0") {
    forAll { max: Int ⇒
      val strat = RetryStrategy.NoDelay(max)
      (0 to max).foreach { i ⇒ assert(strat.delayFor(i) == 0) }
    }
  }

  test("RetryStrategy.None should always have a delay of 0") {
    forAll { max: Int ⇒
      val strat = RetryStrategy.NoDelay(max)
      (0 to max).foreach { i ⇒ assert(strat.delayFor(i) == 0) }
    }
  }

  test("RetryStrategy.Fixed should have a fixed delay") {
    forAll { (max: Int, delay: Long) ⇒
      val strat = RetryStrategy.Fixed(max, delay)
      (0 to max).foreach { i ⇒ assert(strat.delayFor(i) == delay) }
    }
  }

  test("RetryStrategy.Linear should have a linearly increasing delay") {
    forAll { (max: Int, delay: Long) ⇒
      val strat = RetryStrategy.Linear(max, delay)
      (0 to max).foreach { i ⇒ assert(strat.delayFor(i) == i * delay) }
    }
  }

  test("RetryStrategy.Quadratic should have a quadratically increasing delay") {
    forAll { (max: Int, delay: Long) ⇒
      val strat = RetryStrategy.Quadratic(max, delay)
      (0 to max).foreach { i ⇒ assert(strat.delayFor(i) == delay * (i * i)) }
    }
  }
}
