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

import _root_.cats.{Contravariant, Eq}
import kantan.codecs.cats.{CommonInstances, DecoderInstances}

package object cats extends DecoderInstances with CommonInstances {

  // - Eq instances ----------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------

  implicit val xpathCompileErrorEq: Eq[CompileError]          = Eq.fromUniversalEquals
  implicit val xpathReadErrorEq: Eq[ReadError]                = Eq.fromUniversalEquals
  implicit val xpathDecodeErrorEq: Eq[DecodeError]            = Eq.fromUniversalEquals
  implicit val xpathNotFoundEq: Eq[DecodeError.NotFound.type] = Eq.fromUniversalEquals
  implicit val xpathTypeErrorEq: Eq[DecodeError.TypeError]    = Eq.fromUniversalEquals
  implicit val xpathParseErrorEq: Eq[ParseError]              = Eq.fromUniversalEquals
  implicit val xpathSyntaxErrorEq: Eq[ParseError.SyntaxError] = Eq.fromUniversalEquals
  implicit val xpathIOErrorEq: Eq[ParseError.IOError]         = Eq.fromUniversalEquals
  implicit val xpathXPathErrorEq: Eq[XPathError]              = Eq.fromUniversalEquals
  implicit val xpathNodeEq: Eq[Node]                          = Eq.instance((n1, n2) => n1.isEqualNode(n2))

  // - Misc. instances -------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------

  /** `Contravariant` instance for `XmlSource`. */
  implicit val xmlSource: Contravariant[XmlSource] = new Contravariant[XmlSource] {
    override def contramap[A, B](fa: XmlSource[A])(f: B => A) = fa.contramap(f)
  }
}
