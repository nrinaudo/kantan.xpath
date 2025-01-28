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

/** Describes how to retry failed request for [[RemoteXmlSource]].
  *
  * Constructors for commonly used patterns can be found in the [[RetryStrategy$]] companion object.
  *
  * @param max
  *   maximum number of attempts that can be made for a given request.
  * @param delay
  *   delay, in milliseconds, between each attempt.
  * @param factor
  *   function that calculates the delay factor based on the number of requests already attempted. In order to double
  *   waiting times between each attempt, for example, one would pass `i ⇒ 2 ^ i`.
  */
final case class RetryStrategy(max: Int, delay: Long, factor: Int => Int) {
  def delayFor(count: Int): Long =
    factor(count) * delay
}

object RetryStrategy {

  /** Retry strategy that will retry up to `count` times without any delay between attempts. */
  def noDelay(count: Int): RetryStrategy =
    RetryStrategy(count, 0, _ => 0)

  /** No failure will be retried. */
  val none: RetryStrategy = noDelay(0)

  /** Retry strategy that will retry up to `count` times with a fixed delay of `delay` ms between each attempt. */
  def fixed(count: Int, delay: Long): RetryStrategy =
    RetryStrategy(count, delay, _ => 1)

  /** Retry strategy that will retry up to `count` times with a linearly increasing delay between each attempt.
    *
    * The first attempt will be done with a delay of `delay` ms, the second one `2 * delay`, the third `3 * delay`...
    */
  def linear(count: Int, delay: Long): RetryStrategy =
    RetryStrategy(count, delay, identity)

  /** Retry strategy that will retry up to `count` times with a quadratically increasing delay between each attempt.
    *
    * The first attempt will be done with a delay of `delay` ms, the second one `4 * delay`, the third `8 * delay`...
    */
  def quadratic(count: Int, delay: Long): RetryStrategy =
    RetryStrategy(count, delay, i => i * i)
}
