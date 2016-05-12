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

package kantan.xpath.ops

import kantan.xpath._

/** Enriches strings with an [[xpath]] method that attempts to compile them as an XPath expression. */
final class StringOps(val str: String) extends AnyVal {
  /** Attempts to compile the string as an XPath expression. */
  def xpath[A](implicit comp: Compiler[A]): Query[DecodeResult[A]] =
    Query.unsafeCompile(str)
}

trait ToStringOps {
  implicit def toXPathStringOps(str: String): StringOps = new StringOps(str)
}

object string extends ToStringOps
