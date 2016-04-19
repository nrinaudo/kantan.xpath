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

object ops {
  /** Provides syntax for all types that have an implicit instance of [[XmlSource]] in scope.
    *
    * The most common use case is to evaluate an XPath expression directly on a value:
    * {{{
    * val f: java.io.File = ???
    * f.evalXPath[List[java.net.URI]]("//a/@href")
    * }}}
    */
  implicit class XmlSourceOps[A](val a: A) extends AnyVal {
    /** Shorthand for [[XmlSource.asNode]]. */
    def asNode(implicit source: XmlSource[A]): ParseResult = source.asNode(a)

    /** Shorthand for [[XmlSource.asUnsafeNode]]. */
    def asUnsafeNode(implicit source: XmlSource[A]): Node = source.asUnsafeNode(a)

    /** Shorthand for [[XmlSource.unsafeEval[B](a:A,expr:String)*]]. */
    def unsafeEvalXPath[B: Compiler](expr: String)(implicit source: XmlSource[A]): B = source.unsafeEval(a, expr)

    /** Shorthand for [[XmlSource.eval[B](a:A,expr:String)*]]. */
    def evalXPath[B: Compiler](expr: String)(implicit source: XmlSource[A]): XPathResult[B] =
      source.eval(a, expr)

    def unsafeEvalXPath[B](expr: Expression[DecodeResult[B]])(implicit source: XmlSource[A]): B =
      source.unsafeEval(a, expr)

    def evalXPath[B](expr: Expression[DecodeResult[B]])(implicit source: XmlSource[A]): ReadResult[B] =
      source.eval(a, expr)
  }

  implicit class StringOps(val str: String) extends AnyVal {
    def xpath[A](implicit comp: Compiler[A]): Expression[DecodeResult[A]] =
      comp.compile(str).getOrElse(sys.error(s"Not a valid XPath expression: '$str'."))
  }

  implicit class ExpressionOps[A](val expr: Expression[DecodeResult[A]]) extends AnyVal {
    def mapResult[B](f: A ⇒ B): Expression[DecodeResult[B]] = expr.map(_.map(f))
  }
}
