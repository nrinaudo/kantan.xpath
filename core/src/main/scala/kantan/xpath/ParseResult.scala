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

import kantan.codecs.ResultCompanion

/** Provides instance creation methods for [[ParseResult]]. */
object ParseResult extends ResultCompanion.WithDefault[ParseError] {
  override protected def fromThrowable(t: Throwable): ParseError.SyntaxError =
    ParseError.SyntaxError(t)

  /** Attempts to evaluate the specified expression, wrapping errors in a [[ParseError.IOError]].
    *
    * @example
    *   {{{
    * scala> def f: Int = sys.error("something bad happened")
    *
    * scala> ParseResult.io(f)
    * res0: ParseResult[Int] = Left(IOError: something bad happened)
    *   }}}
    */
  def io[A](a: => A): ParseResult[A] =
    ParseError.IOError.safe(a)

  /** Attempts to evaluate the specified expression, wrapping errors in a [[ParseError.SyntaxError]].
    *
    * @example
    *   {{{
    * scala> def f: Int = sys.error("something bad happened")
    *
    * scala> ParseResult.syntax(f)
    * res0: ParseResult[Int] = Left(SyntaxError: something bad happened)
    *   }}}
    */
  def syntax[A](a: => A): ParseResult[A] =
    ParseError.SyntaxError.safe(a)

  /** Evaluates the specified by-name parameter and passes it to the specified parsing function, wrapping any error
    * along the way in a failure.
    */
  def open[A, B](a: => A)(parse: A => ParseResult[B]): ParseResult[B] =
    io(a).flatMap(parse)
}
