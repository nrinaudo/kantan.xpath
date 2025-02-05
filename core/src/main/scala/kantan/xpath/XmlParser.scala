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

import javax.xml.parsers.DocumentBuilderFactory

/** Contract for anything that knows how to parse XML.
  *
  * The default implementation is always in scope and uses the standard java `javax.xml.parsers.DocumentBuilderFactory`,
  * with no special option. This can be overridden to set some options or, more drastically, to change the parsing
  * mechanism entirely. A typical use case is the `nekohtml` module, which needs to take control of the entire parsing
  * process to tidy up messy HTML documents.
  */
trait XmlParser {

  /** Turns the specified `InputSource` into a `Document`. */
  def parse(source: InputSource): ParseResult[Node]
}

/** Declares the default [[XmlParser]] instance in the implicit scope. */
object XmlParser {

  /** Helper creation method, turns the specified function into an `XmlParser`. */
  def apply(f: InputSource => ParseResult[Node]): XmlParser =
    new XmlParser {
      override def parse(source: InputSource) =
        f(source)
    }

  /** Default implementation.
    *
    * This relies on the standard java `javax.xml.parsers.DocumentBuilderFactory`, using whatever default parameters
    * were set.
    */
  implicit val builtIn: XmlParser = {
    val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    XmlParser(source => ParseResult(factory.newDocumentBuilder().parse(source)))
  }
}
