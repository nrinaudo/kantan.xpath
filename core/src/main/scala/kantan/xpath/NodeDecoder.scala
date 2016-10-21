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

import kantan.codecs.{Decoder, DecoderCompanion}
import kantan.codecs.strings.StringDecoder
import kantan.xpath.DecodeError.TypeError

/** Provides instance creation and summoning methods. */
object NodeDecoder extends GeneratedDecoders with DecoderCompanion[Option[Node], DecodeError, codecs.type ] {
  /** Returns an implicit instance of `NodeDecoder[A]` if one is found in scope, fails compilation otherwise. */
  def apply[A](implicit ev: NodeDecoder[A]): NodeDecoder[A] = macro imp.summon[NodeDecoder[A]]

  @deprecated("use from instead (see https://github.com/nrinaudo/kantan.xpath/issues/10)", "0.1.6")
  def apply[A](f: Option[Node] ⇒ DecodeResult[A]): NodeDecoder[A] = Decoder.from(f)

  def fromFound[A](f: Node ⇒ DecodeResult[A]): NodeDecoder[A] = Decoder.from(_.map(f).getOrElse(DecodeResult.notFound))
}

/** Provides default [[NodeDecoder]] instances. */
trait NodeDecoderInstances {
  /** Decodes nodes as nodes. */
    implicit val node: NodeDecoder[Node] = NodeDecoder.fromFound(n ⇒ DecodeResult.success(n))
    /** Decodes nodes as elements. */
    implicit val element: NodeDecoder[Element] = NodeDecoder.fromFound(n ⇒ DecodeResult(n.asInstanceOf[Element]))
    /** Decodes nodes as attributes. */
    implicit val attr: NodeDecoder[Attr] = NodeDecoder.fromFound(n ⇒ DecodeResult(n.asInstanceOf[Attr]))

  /** Turns any of the string decodes provided by kantan.codecs into node decoders. */
  implicit def fromString[A: StringDecoder]: NodeDecoder[A] =
    NodeDecoder.fromFound(n ⇒ StringDecoder[A]
      .mapError(t ⇒ TypeError(t.getMessage, t.getCause)).decode(n.getTextContent))

  implicit def optionNodeDecoder[A: NodeDecoder]: NodeDecoder[Option[A]] =
    Decoder.optionalDecoder

  implicit def eitherNodeDecoder[A: NodeDecoder, B: NodeDecoder]: NodeDecoder[Either[A, B]] =
    Decoder.eitherDecoder
}
