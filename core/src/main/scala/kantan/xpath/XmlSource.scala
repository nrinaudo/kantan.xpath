package kantan.xpath

import java.io._
import java.net.{URI, URL}

import org.xml.sax.InputSource
import simulacrum.{noop, typeclass}

import scala.io.Codec

@typeclass
trait XmlSource[-A] { self =>
  def asNode(a: A): Option[Node]

  def asUnsafeNode(a: A): Node = asNode(a).get

  @noop
  def contramap[B](f: B => A): XmlSource[B] = XmlSource(b => self.asNode(f(b)))
}

object XmlSource {
  def apply[A](f: A => Option[Node]): XmlSource[A] = new XmlSource[A] {
    override def asNode(a: A) = f(a)
  }

  /** Construction method for types that cannot fail to parse as a `Node`. */
  def safe[A](f: A => Node): XmlSource[A] = XmlSource(a => Some(f(a)))

  implicit val node: XmlSource[Node] = XmlSource.safe(n => n)

  implicit def inputSource(implicit parser: XmlParser): XmlSource[InputSource] =
    XmlSource(s => parser.parse(s))

  implicit def reader(implicit parser: XmlParser): XmlSource[Reader] = inputSource.contramap(r => new InputSource(r))
  implicit def inputStream(implicit codec: Codec, parser: XmlParser): XmlSource[InputStream] = reader.contramap(i => new InputStreamReader(i, codec.charSet))
  implicit def file(implicit codec: Codec, parser: XmlParser): XmlSource[File] = inputStream.contramap(f => new FileInputStream(f))
  implicit def string(implicit parser: XmlParser): XmlSource[String] = reader.contramap(s => new StringReader(s))
  implicit def url(implicit codec: Codec, parser: XmlParser): RemoteXmlSource[URL] = RemoteXmlSource(identity)
  implicit def uri(implicit codec: Codec, parser: XmlParser): RemoteXmlSource[URI] = url.contramap(_.toURL)//RemoteXmlSource((u: URI) => u.toURL)
}