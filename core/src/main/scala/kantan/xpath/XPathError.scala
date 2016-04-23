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

/** Describes an error that can occur while dealing with XPath. */
sealed abstract class XPathError extends Product with Serializable

/** Describes a XPath expression compilation error. */
final case class CompileError(cause: Throwable) extends XPathError {
  override def toString: String = s"CompileError(${cause.getMessage})"

  override def equals(obj: Any) = obj match {
    case CompileError(cause2) ⇒ cause.getClass == cause2.getClass
    case _                    ⇒ false
  }

  override def hashCode(): Int = cause.hashCode()
}

/** Describes an error that occurred while parsing and / or decoding XML content. */
sealed abstract class ReadError extends XPathError

/** Describes an error that occurred while decoding some XML content. */
sealed abstract class DecodeError extends ReadError

object DecodeError {
  /** Error that occurs when a single result was requested by an XPath expression, but no node was matched. */
  case object NotFound extends DecodeError

  /** Error that occurs when a node was attempted to be decoded as a type its value is not compatible with. */
  final case class TypeError(cause: Throwable) extends DecodeError {
    override def toString: String = s"TypeError(${cause.getMessage})"

    override def equals(obj: Any) = obj match {
      case TypeError(cause2) ⇒ cause.getClass == cause2.getClass
      case _                 ⇒ false
    }

    override def hashCode(): Int = cause.hashCode()
  }
}

/** Describes errors that occur while parsing XML content. */
sealed abstract class ParseError extends ReadError

object ParseError {
  /** Error that occurs when an XML document is not valid. */
  final case class SyntaxError(cause: Throwable) extends ParseError {
    override def toString: String = s"SyntaxError(${cause.getMessage})"

    override def equals(obj: Any) = obj match {
      case SyntaxError(cause2) ⇒ cause.getClass == cause2.getClass
      case _                   ⇒ false
    }

    override def hashCode(): Int = cause.hashCode()
  }

  /** Error that occurs when something IO related went bad. */
  final case class IOError(cause: Throwable) extends ParseError {
    override def toString: String = s"IOError(${cause.getMessage})"

    override def equals(obj: Any) = obj match {
      case IOError(cause2) ⇒ cause.getClass == cause2.getClass
      case _               ⇒ false
    }

    override def hashCode(): Int = cause.hashCode()
  }
}
