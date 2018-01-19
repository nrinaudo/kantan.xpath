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

package kantan

import kantan.codecs.Decoder

package object xpath {
  // - Codec types -----------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** Type class for types that can be decoded from an XML [[Node]].
    *
    * @documentable
    */
  type NodeDecoder[A] = Decoder[Option[Node], A, DecodeError, codecs.type]

  // - Result types ----------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------

  /** Represents the result of any XPath action.
    *
    * An [[XPathResult]] is either a [[CompileResult]] or a [[ReadResult]].
    *
    * @documentable
    */
  type XPathResult[A] = Either[XPathError, A]

  /** Represents the result of taking a string and compiling it into an XPath query.
    *
    * @documentable
    */
  type CompileResult[A] = Either[CompileError, A]

  /** Represents the result of applying an XPath expression, applying to raw data and turning it into usable types.
    *
    * A [[ReadResult]] is either a [[DecodeResult]] or a [[ParseResult]].
    *
    * @documentable
    */
  type ReadResult[A] = Either[ReadError, A]

  /** Represents the result of taking some raw XPath result and turning it into a usable type.
    *
    * Creation methods can be found in the [[DecodeResult$ companion object]].
    *
    * @documentable
    */
  type DecodeResult[A] = Either[DecodeError, A]

  /** Represents the result of taking some raw data and turning it into a usable type.
    *
    * Creation methods can be found in the [[ParseResult$ companion object]].
    *
    * @documentable
    */
  type ParseResult[A] = Either[ParseError, A]

  // - XML types -------------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  type Element         = org.w3c.dom.Element
  type Node            = org.w3c.dom.Node
  type Document        = org.w3c.dom.Document
  type Attr            = org.w3c.dom.Attr
  type NodeList        = org.w3c.dom.NodeList
  type InputSource     = org.xml.sax.InputSource
  type XPathExpression = javax.xml.xpath.XPathExpression
}
