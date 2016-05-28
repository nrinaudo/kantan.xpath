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

import java.io._
import java.net.{URI, URL}
import java.nio.file.{Files, Path}
import scala.io.Codec

/** Type class for turning instances of `A` into valid instances of [[Node]].
  *
  * While it's certainly possible, instances of [[XmlSource]] are rarely used directly. The preferred, idiomatic way
  * is to use the implicit syntax provided by [[kantan.xpath.ops.XmlSourceOps XmlSourceOps]], brought in scope by
  * importing `kantan.xpath.ops._`.
  *
  * See the [[XmlSource$ companion object]] for construction methods and default instances.
  */
trait XmlSource[-A] extends Serializable { self ⇒
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
  def asUnsafeNode(a: A): Node = asNode(a).get

  /** Compiles the specified XPath expression and evaluates it against specified value. */
  def unsafeEval[B](a: A, expr: XPathExpression)(implicit cmp: Compiler[B]): B =
    eval(a, expr).get

  /** Compiles the specified XPath expression and evaluates it against the specified value. */
  def eval[B](a: A, expr: XPathExpression)(implicit cmp: Compiler[B]): XPathResult[B] =
    eval(a, cmp.compile(expr))

  /** Evaluates the specified XPath expression against specified value. */
  def unsafeEval[B](a: A, expr: Query[DecodeResult[B]]): B = eval(a, expr).get

  /** Evaluates the specified XPath expression against specified value. */
  def eval[B](a: A, expr: Query[DecodeResult[B]]): ReadResult[B] = for {
    node ← asNode(a)
    b    ← expr.eval(node)
  } yield b

  /** Turns an `XmlSource[A]` into an `XmlSource[B]`.
    *
    * @see [[contramapResult]].
    */
  def contramap[B](f: B ⇒ A): XmlSource[B] = XmlSource(b ⇒ self.asNode(f(b)))

  /** Turns an `XmlSource[A]` into an `XmlSource[B]`.
    *
    * @see [[contramap]]
    */
  def contramapResult[B](f: B ⇒ ParseResult[A]): XmlSource[B] = XmlSource((b: B) ⇒ f(b).flatMap(self.asNode))
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
object XmlSource {
  /** Summons an [[XmlSource]] instance if one can be found. */
  def apply[A](implicit s: XmlSource[A]): XmlSource[A] = s

  /** Turns the specified function into a new [[XmlSource]] instance. */
  def apply[A](f: A ⇒ ParseResult[Node]): XmlSource[A] = new XmlSource[A] {
    override def asNode(a: A) = f(a)
  }

  /** Turns a [[Node]] into a source of XML data. */
  implicit val node: XmlSource[Node] = XmlSource(n ⇒ ParseResult.success(n))

  /** Turns an [[InputSource]] into a source of XML data. */
  implicit def inputSource(implicit parser: XmlParser): XmlSource[InputSource] =
    XmlSource(s ⇒ parser.parse(s))

  /** Turns a `Reader` into a source of XML data. */
  implicit def reader(implicit parser: XmlParser): XmlSource[Reader] = inputSource.contramap(r ⇒ new InputSource(r))

  /** Turns an `InputStream` into a source of XML data. */
  implicit def inputStream(implicit codec: Codec, parser: XmlParser): XmlSource[InputStream] =
    reader.contramapResult(i ⇒ ParseResult(new InputStreamReader(i, codec.charSet)))

  /** Turns a `File` into a source of XML data. */
  implicit def file(implicit codec: Codec, parser: XmlParser): XmlSource[File] =
    inputStream.contramapResult(f ⇒ ParseResult(new FileInputStream(f)))

  /** Turns a `String` into a source of XML data. */
  implicit def string(implicit parser: XmlParser): XmlSource[String] = reader.contramap(s ⇒ new StringReader(s))

  /** Turns an `URL` into a source of XML data. */
  implicit def url(implicit codec: Codec, parser: XmlParser): RemoteXmlSource[URL] = RemoteXmlSource(u ⇒ ParseResult(u))

  /** Turns an `URI` into a source of XML data. */
  implicit def uri(implicit codec: Codec, parser: XmlParser): RemoteXmlSource[URI] = url.contramap(_.toURL)

  /** Turns an `Array[Byte]` into a source of XML data. */
  implicit def bytes(implicit codec: Codec, parser: XmlParser): XmlSource[Array[Byte]] =
    inputStream.contramap(bs ⇒ new ByteArrayInputStream(bs))

  /** Turns an `Array[Char]` into a source of XML data. */
  implicit def chars(implicit parser: XmlParser): XmlSource[Array[Char]] =
    reader.contramap(cs ⇒ new CharArrayReader(cs))

  /** Turns a `Path` into a source of XML data. */
  implicit def path(implicit codec: Codec): XmlSource[Path] =
    reader.contramapResult(p ⇒ ParseResult(Files.newBufferedReader(p, codec.charSet)))
}
