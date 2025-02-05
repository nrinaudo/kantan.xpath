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

import kantan.codecs.resource.InputResource
import kantan.codecs.resource.ReaderResource

import java.io._

/** Type class for turning instances of `A` into valid instances of [[Node]].
  *
  * While it's certainly possible, instances of [[XmlSource]] are rarely used directly. The preferred, idiomatic way is
  * to use the implicit syntax provided by [[kantan.xpath.ops.XmlSourceOps XmlSourceOps]], brought in scope by importing
  * `kantan.xpath.ops._`.
  *
  * See the [[XmlSource$ companion object]] for construction methods and default instances.
  */
trait XmlSource[-A] extends Serializable { self =>

  /** Turns the specified value into a [[Node]].
    *
    * Results are wrapped in a [[ParseResult]], which makes this method safe. For an unsafe alternative, see
    * [[asUnsafeNode]].
    */
  def asNode(a: A): ParseResult[Node]

  /** Turns the specified value into a [[Node]].
    *
    * This method is unsafe - it will throw an exception should any error occur during parsing. For a safe alternative,
    * see [[asNode]].
    */
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def asUnsafeNode(a: A): Node =
    asNode(a).fold(e => throw e, identity)

  /** Compiles the specified XPath expression and evaluates it against specified value. */
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def unsafeEval[B: Compiler](a: A, expr: XPathExpression): B =
    eval(a, expr).fold(e => throw e, identity)

  /** Compiles the specified XPath expression and evaluates it against the specified value. */
  def eval[B: Compiler](a: A, expr: XPathExpression): XPathResult[B] =
    eval(a, Compiler[B].compile(expr))

  /** Evaluates the specified XPath expression against specified value. */
  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def unsafeEval[B](a: A, expr: Query[DecodeResult[B]]): B =
    eval(a, expr).fold(e => throw e, identity)

  /** Evaluates the specified XPath expression against specified value. */
  def eval[B](a: A, expr: Query[DecodeResult[B]]): ReadResult[B] =
    for {
      node <- asNode(a)
      b    <- expr.eval(node)
    } yield b

  /** Turns an `XmlSource[A]` into an `XmlSource[B]`.
    *
    * @see
    *   [[contramapResult]].
    */
  def contramap[B](f: B => A): XmlSource[B] =
    XmlSource.from(f.andThen(self.asNode))

  /** Turns an `XmlSource[A]` into an `XmlSource[B]`.
    *
    * @see
    *   [[contramap]]
    */
  def contramapResult[AA <: A, B](f: B => ParseResult[AA]): XmlSource[B] =
    XmlSource.from((b: B) => f(b).flatMap(self.asNode))
}

/** Defines convenience methods for creating and summoning [[XmlSource]] instances.
  *
  * Default implementation of standard types are declared here, always bringing them in scope without requirnig an
  * explicit import.
  *
  * The default implementations can also be useful when writing new instances: if you need to write an `XmlSource[T]`
  * and already have an `XmlSource[S]` and a `T ⇒ S`, you just need to call [[XmlSource.contramap]] to get your
  * implementation.
  */
object XmlSource extends LowPriorityXmlSourceInstances {

  /** Summons an [[XmlSource]] instance if one can be found. */
  def apply[A](implicit ev: XmlSource[A]): XmlSource[A] =
    macro imp.summon[XmlSource[A]]

  /** Turns the specified function into a new [[XmlSource]] instance. */
  def from[A](f: A => ParseResult[Node]): XmlSource[A] =
    new XmlSource[A] {
      override def asNode(a: A) =
        f(a)
    }

  /** Turns a [[Node]] into a source of XML data. */
  implicit val node: XmlSource[Node] = XmlSource.from(ParseResult.success)

  /** Turns an [[InputSource]] into a source of XML data. */
  implicit def inputSource(implicit parser: XmlParser): XmlSource[InputSource] =
    XmlSource.from(parser.parse)

  implicit def fromInputResource[A: InputResource](implicit parser: XmlParser): XmlSource[A] =
    inputSource.contramapResult { a =>
      InputResource[A]
        .open(a)
        .map(r => new InputSource(r))
        .left
        .map(e => ParseError.IOError(e.getMessage, e.getCause))
    }
}

trait LowPriorityXmlSourceInstances {
  // Low priority since it assumes the encoding
  implicit def fromReaderResource[A: ReaderResource](implicit parser: XmlParser): XmlSource[A] =
    XmlSource.from(parser.parse).contramapResult { a =>
      ReaderResource[A]
        .open(a)
        .map(r => new InputSource(r))
        .left
        .map(e => ParseError.IOError(e.getMessage, e.getCause))
    }
}
