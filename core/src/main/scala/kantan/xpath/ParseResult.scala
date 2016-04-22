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

object ParseResult {
  def open[A](acquire: ⇒ A)(parse: A ⇒ ParseResult): ParseResult =
    Result.nonFatal(acquire).leftMap(ParseError.IOError).flatMap(parse)

  def success(node: Node): ParseResult = Success(node)

  def apply(node: ⇒ Node): ParseResult = Result.nonFatal(node).leftMap(ParseError.SyntaxError.apply)
}
