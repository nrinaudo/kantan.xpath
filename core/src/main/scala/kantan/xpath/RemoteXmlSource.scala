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
import java.net.URLConnection

/** [[XmlSource]] implementation anything that can be turned into a `java.io.URL`.
  *
  * The main purpose here is to allow application developers to set their own HTTP headers: when scraping websites, it's
  * typically necessary to change the default user agent to something a bit more browser-like.
  */
@SuppressWarnings(Array("org.wartremover.warts.ForeachEntry"))
final case class RemoteXmlSource[A](toURL: A => ParseResult[URL], retry: RetryStrategy, headers: Map[String, String])(
  implicit parser: XmlParser
) extends XmlSource[A] {
  private def open(url: URL): URLConnection = {
    val con = url.openConnection()
    headers.foreach { case (n, v) => con.setRequestProperty(n, v) }
    con
  }

  @SuppressWarnings(Array("org.wartremover.warts.ThreadSleep"))
  private def download(url: URL, count: Int): ParseResult[Node] =
    (for {
      con <- ParseResult(open(url))
      res <- ParseResult.open {
               con.connect()
               new InputSource(con.getInputStream)
             }(parser.parse)
    } yield res).left.flatMap {
      case ParseError.IOError(_) if retry.max > count =>
        Thread.sleep(retry.delayFor(count))
        download(url, count + 1)
      case other => Left(other)
    }

  override def asNode(a: A): ParseResult[Node] =
    toURL(a).flatMap(url => download(url, 0))

  override def contramap[B](f: B => A): RemoteXmlSource[B] =
    copy(toURL = f.andThen(toURL))

  override def contramapResult[AA <: A, B](f: B => ParseResult[AA]): RemoteXmlSource[B] =
    copy(toURL = (b: B) => f(b).flatMap(toURL))

  /** Sets the specified header to the specified value. */
  def withHeader(name: String, value: String): RemoteXmlSource[A] =
    copy(headers = headers + (name -> value))

  /** Sets the user-agent to use whenever connecting to a URL. */
  def withUserAgent(value: String): RemoteXmlSource[A] =
    withHeader("User-Agent", value)

  def withRetry(strategy: RetryStrategy): RemoteXmlSource[A] =
    copy(retry = strategy)
}
