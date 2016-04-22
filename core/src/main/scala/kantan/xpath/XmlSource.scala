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
import scala.io.Codec

trait XmlSource[-A] extends Serializable { self ⇒
  def asNode(a: A): ParseResult

  def asUnsafeNode(a: A): Node = asNode(a).get

  def unsafeEval[B](a: A, expr: String)(implicit cmp: Compiler[B]): B =
    eval(a, expr).get

  def eval[B](a: A, expr: String)(implicit cmp: Compiler[B]): XPathResult[B] =
    cmp.compile(expr).flatMap(e ⇒ eval(a, e))

  def unsafeEval[B](a: A, expr: Query[DecodeResult[B]]): B = eval(a, expr).get

  def eval[B](a: A, expr: Query[DecodeResult[B]]): ReadResult[B] = for {
    node ← asNode(a)
    b    ← expr(node)
  } yield b

  def contramap[B](f: B ⇒ A): XmlSource[B] = XmlSource(b ⇒ self.asNode(f(b)))
}

object XmlSource {
  def apply[A](implicit s: XmlSource[A]): XmlSource[A] = s

  def apply[A](f: A ⇒ ParseResult): XmlSource[A] = new XmlSource[A] {
    override def asNode(a: A) = f(a)
  }

  implicit val node: XmlSource[Node] = XmlSource(n ⇒ ParseResult.success(n))

  implicit def inputSource(implicit parser: XmlParser): XmlSource[InputSource] =
    XmlSource(s ⇒ parser.parse(s))

  implicit def reader(implicit parser: XmlParser): XmlSource[Reader] = inputSource.contramap(r ⇒ new InputSource(r))
  implicit def inputStream(implicit codec: Codec, parser: XmlParser): XmlSource[InputStream] =
    reader.contramap(i ⇒ new InputStreamReader(i, codec.charSet))
  implicit def file(implicit codec: Codec, parser: XmlParser): XmlSource[File] =
    inputStream.contramap(f ⇒ new FileInputStream(f))
  implicit def string(implicit parser: XmlParser): XmlSource[String] = reader.contramap(s ⇒ new StringReader(s))
  implicit def url(implicit codec: Codec, parser: XmlParser): RemoteXmlSource[URL] = RemoteXmlSource(identity)
  implicit def uri(implicit codec: Codec, parser: XmlParser): RemoteXmlSource[URI] = url.contramap(_.toURL)
}
