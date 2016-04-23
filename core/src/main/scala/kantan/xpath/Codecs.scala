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

import kantan.codecs.strings.StringDecoder
import kantan.xpath.DecodeError.TypeError

/** Provides default [[NodeDecoder]] instances. */
object codecs {
  /** Decodes nodes as nodes. */
  implicit val node: NodeDecoder[Node] = NodeDecoder(n ⇒ DecodeResult.success(n))
  /** Decodes nodes as elements. */
  implicit val element: NodeDecoder[Element] = NodeDecoder(n ⇒ DecodeResult(n.asInstanceOf[Element]))
  /** Decodes nodes as attributes. */
  implicit val attr: NodeDecoder[Attr] = NodeDecoder(n ⇒ DecodeResult(n.asInstanceOf[Attr]))

  /** Turns any of the string decodes provided by kantan.codecs into node decoders. */
  implicit def fromString[A](implicit da: StringDecoder[A]): NodeDecoder[A] =
    da.tag[codecs.type].contramapEncoded((n: Node) ⇒ n.getTextContent).mapError(TypeError.apply)
}
