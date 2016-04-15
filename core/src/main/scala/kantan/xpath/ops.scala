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
  implicit class XmlSourceOps[A](val a: A) extends AnyVal {
    def asNode(implicit source: XmlSource[A]): ParseResult = source.asNode(a)

    def first[B](expr: Expression[DecodeResult[B]])(implicit source: XmlSource[A]): ReadResult[B] =
      source.first(a, expr)

    def first[B: NodeDecoder](expr: String)(implicit compiler: Compiler, source: XmlSource[A]): ReadResult[B] =
      source.first(a, expr)

    def all[F[X] <: TraversableOnce[X], B](expr: Expression[DecodeResult[B]])
                                          (implicit s: XmlSource[A],
                                           cbf1: CanBuildFrom[Nothing, DecodeResult[B], F[DecodeResult[B]]],
                                           cbf2: CanBuildFrom[F[DecodeResult[B]], B, F[B]]): ReadResult[F[B]] =
      s.all(a, expr)

    def all[F[X] <: TraversableOnce[X], B: NodeDecoder](expr: String)
                                          (implicit s: XmlSource[A], compiler: Compiler,
                                           cbf1: CanBuildFrom[Nothing, DecodeResult[B], F[DecodeResult[B]]],
                                           cbf2: CanBuildFrom[F[DecodeResult[B]], B, F[B]]): ReadResult[F[B]] =
      s.all(a, expr)
  }

  implicit class StringOps(val str: String) extends AnyVal {
    def xpath[A: NodeDecoder](implicit comp: Compiler): Expression[DecodeResult[A]] =
      comp.compile[A](str).getOrElse(sys.error(s"Not a valid XPath expression: '$str'."))
  }
}
