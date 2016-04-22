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

import kantan.codecs.Result
import kantan.codecs.Result.Success

/** Provides instance creation methods for [[ParseResult]]. */
object ParseResult {
  /** Creates a success with the specified value. */
  def success[A](a: A): ParseResult[A] = Success(a)

  /** Evaluates the specified by-name parameter, wrapping it in a success if successful or a
    * failure otherwise.
    */
  def apply[A](a: ⇒ A): ParseResult[A] = Result.nonFatal(a).leftMap(ParseError.SyntaxError.apply)

  /** Evaluates the specified by-name parameter and passes it to the specified parsing function, wrapping any error
    * along the way in a failure.
    */
  def open[A, B](a: ⇒ A)(parse: A ⇒ ParseResult[B]): ParseResult[B] =
    Result.nonFatal(a).leftMap(ParseError.IOError).flatMap(parse)
}
