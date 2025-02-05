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

/** Provides syntax for all types that have an implicit instance of [[XmlSource]] in scope.
  *
  * The most common use case is to evaluate an XPath expression directly on a value:
  * {{{
  * val f: java.io.File = ???
  * f.evalXPath[List[java.net.URI]]("//a/@href")
  * }}}
  */
final class XmlSourceOps[A: XmlSource](val a: A) {

  /** Shorthand for [[XmlSource.asNode]]. */
  def asNode: ParseResult[Node] =
    XmlSource[A].asNode(a)

  /** Shorthand for [[XmlSource.asUnsafeNode]]. */
  def asUnsafeNode: Node =
    XmlSource[A].asUnsafeNode(a)

  def unsafeEvalXPath[B: Compiler](expr: XPathExpression): B =
    XmlSource[A].unsafeEval(a, expr)

  def evalXPath[B: Compiler](expr: XPathExpression): XPathResult[B] =
    XmlSource[A].eval(a, expr)

  def unsafeEvalXPath[B](expr: Query[DecodeResult[B]]): B =
    XmlSource[A].unsafeEval(a, expr)

  def evalXPath[B](expr: Query[DecodeResult[B]]): ReadResult[B] =
    XmlSource[A].eval(a, expr)
}

trait ToXmlSourceOps {
  implicit def toXmlSourceOps[A: XmlSource](a: A): XmlSourceOps[A] =
    new XmlSourceOps(a)
}

object xmlSource extends ToXmlSourceOps
