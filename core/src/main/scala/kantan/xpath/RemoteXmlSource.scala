package kantan.xpath

import java.net.URL

import org.xml.sax.InputSource

case class RemoteXmlSource[A](toURL: A => URL, headers: Map[String, String] = Map.empty)(implicit parser: XmlParser) extends XmlSource[A] {
  override def asNode(a: A): Option[Node] = {
    val con = toURL(a).openConnection()
    headers.foreach { case (n, v) => con.setRequestProperty(n, v) }
    con.connect()
    parser.parse(new InputSource(con.getInputStream))
  }

  override def contramap[B](f: B => A): RemoteXmlSource[B] = copy(toURL = f andThen toURL)

  def withHeader(name: String, value: String): RemoteXmlSource[A] =
    copy(headers = headers + (name -> value))
}
