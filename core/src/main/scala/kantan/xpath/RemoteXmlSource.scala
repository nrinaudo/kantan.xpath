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

import java.net.URL

/** [[XmlSource]] implementation anything that can be turned into a `java.io.URL`.
  *
  * The main purpose here is to allow application developers to set their own HTTP headers: when scrapping websites,
  * it's typically necessary to change the default user agent to something a bit more browser-like.
  */
case class RemoteXmlSource[A](toURL: A ⇒ URL, headers: Map[String, String] = Map.empty)(implicit parser: XmlParser)
  extends XmlSource[A] {
  override def asNode(a: A): ParseResult = {
    val con = toURL(a).openConnection()
    headers.foreach { case (n, v) ⇒ con.setRequestProperty(n, v) }

    ParseResult.open {
      con.connect()
      new InputSource(con.getInputStream)
    }(parser.parse)
  }

  override def contramap[B](f: B ⇒ A): RemoteXmlSource[B] = copy(toURL = f andThen toURL)

  /** Sets the specified header to the specified value. */
  def withHeader(name: String, value: String): RemoteXmlSource[A] =
    copy(headers = headers + (name → value))

  /** Sets the user-agent to use whenever connecting to a URL. */
  def withUserAgent(value: String): RemoteXmlSource[A] = withHeader("User-Agent", value)
}
