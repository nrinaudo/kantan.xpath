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

import scala.collection.generic.CanBuildFrom

object ops {
  implicit class RichSource[A](val a: A) extends AnyVal {
    def asUnsafeNode(implicit source: XmlSource[A]): Node = source.asUnsafeNode(a)
    def asNode(implicit source: XmlSource[A]): ParseResult = source.asNode(a)
    def first[B: NodeDecoder](expr: Expression)(implicit source: XmlSource[A]): ReadResult[B] = source.first(a, expr)
    def all[F[_], B: NodeDecoder](expr: Expression)
                                 (implicit s: XmlSource[A], cbf: CanBuildFrom[Nothing, B, F[B]]): ReadResult[F[B]] =
      s.all(a, expr)
  }

  implicit class RichString(val str: String) extends AnyVal {
    def xpath(implicit comp: XPathCompiler): Expression =
      Expression(str)(comp).getOrElse(sys.error(s"Not a valid XPath expression: '$str'."))
  }
}
