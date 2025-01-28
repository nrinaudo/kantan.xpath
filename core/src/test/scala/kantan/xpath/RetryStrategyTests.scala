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

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class RetryStrategyTests extends AnyFunSuite with ScalaCheckPropertyChecks with Matchers {
  implicit val arbInt: Arbitrary[Int]   = Arbitrary(Gen.choose(0, 100))
  implicit val arbLong: Arbitrary[Long] = Arbitrary(Gen.choose(0L, 24 * 60 * 60 * 1000L))

  test("RetryStrategy.noDelay should always have a delay of 0") {
    forAll { max: Int =>
      val strat = RetryStrategy.noDelay(max)
      (0 to max).foreach { i =>
        strat.delayFor(i) should be(0)
      }
    }
  }

  test("RetryStrategy.none should always have a delay of 0") {
    forAll { max: Int =>
      val strat = RetryStrategy.none
      (0 to max).foreach { i =>
        strat.delayFor(i) should be(0)
      }
    }
  }

  test("RetryStrategy.fixed should have a fixed delay") {
    forAll { (max: Int, delay: Long) =>
      val strat = RetryStrategy.fixed(max, delay)
      (0 to max).foreach { i =>
        strat.delayFor(i) should be(delay)
      }
    }
  }

  test("RetryStrategy.linear should have a linearly increasing delay") {
    forAll { (max: Int, delay: Long) =>
      val strat = RetryStrategy.linear(max, delay)
      (0 to max).foreach { i =>
        strat.delayFor(i) should be(i * delay)
      }
    }
  }

  test("RetryStrategy.quadratic should have a quadratically increasing delay") {
    forAll { (max: Int, delay: Long) =>
      val strat = RetryStrategy.quadratic(max, delay)
      (0 to max).foreach { i =>
        strat.delayFor(i) should be(delay * (i * i))
      }
    }
  }
}
