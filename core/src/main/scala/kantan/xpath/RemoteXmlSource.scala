package kantan.xpath

import java.net.URL

import org.xml.sax.InputSource

/** [[XmlSource]] implementation anything that can be turned into a `java.io.URL`.
  *
  * The main purpose here is to allow application developers to set their own HTTP headers: when scrapping websites,
  * it's typically necessary to change the default user agent to something a bit more browser-like.
  */
case class RemoteXmlSource[A](toURL: A => URL, headers: Map[String, String] = Map.empty)(implicit parser: XmlParser) extends XmlSource[A] {
  override def asNode(a: A): DecodeResult[Node] = {
    val con = toURL(a).openConnection()
    headers.foreach { case (n, v) => con.setRequestProperty(n, v) }
    DecodeResult {
      con.connect()
      new InputSource(con.getInputStream)
    }.flatMap(parser.parse)
  }

  override def contramap[B](f: B => A): RemoteXmlSource[B] = copy(toURL = f andThen toURL)

  /** Sets the specified header to the specified value. */
  def withHeader(name: String, value: String): RemoteXmlSource[A] =
    copy(headers = headers + (name -> value))

  /** Sets the user-agent to use whenever connecting to a URL. */
  def withUserAgent(value: String): RemoteXmlSource[A] = withHeader("User-Agent", value)
}
