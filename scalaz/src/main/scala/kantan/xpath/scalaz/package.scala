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

import _root_.scalaz.{Contravariant, Equal}
import kantan.codecs.scalaz.{CommonInstances, DecoderInstances}

package object scalaz extends DecoderInstances with CommonInstances {
  // - Equal instances for errors --------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------

  implicit val xpathCompileErrorEqual: Equal[CompileError]          = Equal.equalA
  implicit val xpathReadErrorEqual: Equal[ReadError]                = Equal.equalA
  implicit val xpathDecodeErrorEqual: Equal[DecodeError]            = Equal.equalA
  implicit val xpathNotFoundEqual: Equal[DecodeError.NotFound.type] = Equal.equalA
  implicit val xpathTypeErrorEqual: Equal[DecodeError.TypeError]    = Equal.equalA
  implicit val xpathParseErrorEqual: Equal[ParseError]              = Equal.equalA
  implicit val xpathSyntaxErrorEqual: Equal[ParseError.SyntaxError] = Equal.equalA
  implicit val xpathIOErrorEqual: Equal[ParseError.IOError]         = Equal.equalA
  implicit val xpathXPathErrorEqual: Equal[XPathError]              = Equal.equalA
  implicit val xpathNodeEqual: Equal[Node]                          = Equal.equal((n1, n2) => n1.isEqualNode(n2))

  // - Misc. instances -------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** `Contravariant` instance for `XmlSource`. */
  implicit val xmlSource: Contravariant[XmlSource] = new Contravariant[XmlSource] {
    override def contramap[A, B](fa: XmlSource[A])(f: B => A) = fa.contramap(f)
  }
}
