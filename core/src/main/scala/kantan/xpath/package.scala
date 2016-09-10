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

import kantan.codecs.{Decoder, Result}

package object xpath {
  // - Codec types -----------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** Type class for types that can be decoded from an XML [[Node]]. */
  type NodeDecoder[A] = Decoder[Option[Node], A, DecodeError, codecs.type]



  // - Result types ----------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  type Success[A] = Result.Success[A]
  val Success = Result.Success

  type Failure[A] = Result.Failure[A]
  val Failure = Result.Failure

  /** Represents the result of any XPath action.
    *
    * An [[XPathResult]] is either a [[CompileResult]] or a [[ReadResult]].
    *
    * @see kantan.codecs.Result
    */
  type XPathResult[A] = Result[XPathError, A]

  /** Represents the result of taking a string and compiling it into an XPath query.
    *
    * @see kantan.codecs.Result
    */
  type CompileResult[A] = Result[CompileError, A]

  /** Represents the result of applying an XPath expression, applying to raw data and turning it into usable types.
    *
    * A [[ReadResult]] is either a [[DecodeResult]] or a [[ParseResult]].
    *
    * @see kantan.codecs.Result
    */
  type ReadResult[A] = Result[ReadError, A]

  /** Represents the result of taking some raw XPath result and turning it into a usable type.
    *
    * Creation methods can be found in the [[DecodeResult$ companion object]].
    *
    * @see kantan.codecs.Result
    */
  type DecodeResult[A] = Result[DecodeError, A]

  /** Represents the result of taking some raw data and turning it into a usable type.
    *
    * Creation methods can be found in the [[ParseResult$ companion object]].
    *
    * @see kantan.codecs.Result
    */
  type ParseResult[A] = Result[ParseError, A]



  // - XML types -------------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  type Element = org.w3c.dom.Element
  type Node = org.w3c.dom.Node
  type Document = org.w3c.dom.Document
  type Attr = org.w3c.dom.Attr
  type NodeList = org.w3c.dom.NodeList
  type InputSource = org.xml.sax.InputSource
  type XPathExpression = javax.xml.xpath.XPathExpression
}
