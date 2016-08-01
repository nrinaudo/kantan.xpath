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
sealed abstract class XPathError extends Exception with Product with Serializable

/** Describes a XPath expression compilation error. */
sealed case class CompileError(message: String) extends XPathError {
  override final val getMessage = message
}

object CompileError {
  def apply(msg: String, t: Throwable): CompileError = new CompileError(msg) {
    override val getCause = t
  }

  def apply(t: Throwable): CompileError = CompileError(Option(t.getMessage).getOrElse("Compile error"), t)
}

/** Describes an error that occurred while parsing and / or decoding XML content. */
sealed abstract class ReadError extends XPathError

/** Describes an error that occurred while decoding some XML content. */
sealed abstract class DecodeError extends ReadError

object DecodeError {
  /** Error that occurs when a single result was requested by an XPath expression, but no node was matched. */
  final case class NotFound() extends DecodeError

  /** Error that occurs when a node was attempted to be decoded as a type its value is not compatible with. */
  sealed case class TypeError(message: String) extends DecodeError {
    override final val getMessage = message
  }

  object TypeError {
    def apply(msg: String, t: Throwable): TypeError = new TypeError(msg) {
      override val getCause = t
    }

    def apply(t: Throwable): TypeError = TypeError(Option(t.getMessage).getOrElse("Type error"), t)
  }
}

/** Describes errors that occur while parsing XML content. */
sealed abstract class ParseError extends ReadError

object ParseError {
  /** Error that occurs when an XML document is not valid. */
  sealed case class SyntaxError(message: String) extends ParseError {
    override final val getMessage = message
  }

  object SyntaxError {
    def apply(msg: String, t: Throwable): SyntaxError = new SyntaxError(msg) {
      override val getCause = t
    }

    def apply(t: Throwable): SyntaxError = SyntaxError(Option(t.getMessage).getOrElse("Syntax error"), t)
  }

  /** Error that occurs when something IO related went bad. */
  sealed case class IOError(message: String) extends ParseError {
    override val getMessage = message
  }

  object IOError {
    def apply(msg: String, t: Throwable): IOError = new IOError(msg) {
      override val getCause = t
    }

    def apply(t: Throwable): IOError = IOError(Option(t.getMessage).getOrElse("IO error"), t)
  }
}
