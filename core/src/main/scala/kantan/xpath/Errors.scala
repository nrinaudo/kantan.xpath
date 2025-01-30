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

import kantan.codecs.error.{Error, ErrorCompanion}

/** Describes an error that can occur while dealing with XPath. */
sealed abstract class XPathError(msg: String) extends Error(msg)

/** Describes a XPath expression compilation error. */
sealed case class CompileError(message: String) extends XPathError(message)

object CompileError extends ErrorCompanion("an unspecified compilation error occurred")(s => new CompileError(s))

/** Describes an error that occurred while parsing and / or decoding XML content. */
sealed abstract class ReadError(msg: String) extends XPathError(msg)

/** Describes an error that occurred while decoding some XML content. */
sealed abstract class DecodeError(msg: String) extends ReadError(msg)

object DecodeError {

  /** Error that occurs when a single result was requested by an XPath expression, but no node was matched. */
  @SuppressWarnings(Array("org.wartremover.warts.ObjectThrowable"))
  case object NotFound extends DecodeError("no matched node")

  /** Error that occurs when a node was attempted to be decoded as a type its value is not compatible with. */
  sealed case class TypeError(message: String) extends DecodeError(message)

  object TypeError extends ErrorCompanion("an unspecified type error occurred")(s => new TypeError(s))
}

/** Describes errors that occur while parsing XML content. */
sealed abstract class ParseError(msg: String) extends ReadError(msg)

object ParseError {

  /** Error that occurs when an XML document is not valid. */
  sealed case class SyntaxError(message: String) extends ParseError(message)

  object SyntaxError extends ErrorCompanion("an unspecified syntax error occurred")(s => new SyntaxError(s))

  /** Error that occurs when something IO related went bad. */
  sealed case class IOError(message: String) extends ParseError(message)

  object IOError extends ErrorCompanion("an unspecified IO error occurred")(s => new IOError(s))
}
