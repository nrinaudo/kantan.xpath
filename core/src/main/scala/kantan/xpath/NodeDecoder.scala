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

import kantan.codecs.Decoder
import kantan.codecs.strings._
import kantan.xpath.DecodeError.TypeError

/** Provides instance creation and summoning methods. */
object NodeDecoder extends GeneratedDecoders {
  /** Returns an implicit instance of `NodeDecoder[A]` if one is found in scope, fails compilation otherwise. */
  def apply[A](implicit da: NodeDecoder[A]): NodeDecoder[A] = da

  /** Creates a new [[NodeDecoder]] from the specified function. */
  def apply[A](f: Option[Node] ⇒ DecodeResult[A]): NodeDecoder[A] = Decoder(f)

  def fromFound[A](f: Node ⇒ DecodeResult[A]): NodeDecoder[A] = Decoder(_.map(f).getOrElse(DecodeResult.notFound))
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
  implicit def fromString[A](implicit da: StringDecoder[A]): NodeDecoder[A] =
    NodeDecoder.fromFound(n ⇒ da.mapError(TypeError.apply).decode(n.getTextContent))

  implicit def optionNodeDecoder[A: NodeDecoder]: NodeDecoder[Option[A]] =
    Decoder.optionalDecoder

  implicit def eitherNodeDecoder[A: NodeDecoder, B: NodeDecoder]: NodeDecoder[Either[A, B]] =
    Decoder.eitherDecoder
}
