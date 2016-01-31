package grind

import java.io._
import java.net.{URI, URL}

import org.xml.sax.InputSource
import simulacrum.typeclass

import scala.io.Codec

@typeclass
trait XmlSource[-A] { self =>
  def asNode(a: A): Node
  def contramap[B](f: B => A): XmlSource[B] = XmlSource(b => self.asNode(f(b)))

  // TODO: helper methods to execute XPath expressions directly.
  //def evaluate[B: NodeDecoder](a: A, expr: Expression): B = expr.evaluate(asNode(a))
}

object XmlSource {
  def apply[A](f: A => Node): XmlSource[A] = new XmlSource[A] {
    override def asNode(a: A) = f(a)
  }

  implicit val node: XmlSource[Node] = XmlSource(n => n)

  implicit def inputSource(implicit parser: XmlParser): XmlSource[InputSource] =
    XmlSource(s => parser.parse(s))

  implicit def reader(implicit parser: XmlParser): XmlSource[Reader] = inputSource.contramap(r => new InputSource(r))
  implicit def inputStream(implicit codec: Codec, parser: XmlParser): XmlSource[InputStream] = reader.contramap(i => new InputStreamReader(i, codec.charSet))
  implicit def file(implicit codec: Codec, parser: XmlParser): XmlSource[File] = inputStream.contramap(f => new FileInputStream(f))
  implicit def string(implicit parser: XmlParser): XmlSource[String] = reader.contramap(s => new StringReader(s))
  implicit def url(implicit codec: Codec, parser: XmlParser): XmlSource[URL] = inputStream.contramap(u => u.openStream())
  implicit def uri(implicit codec: Codec, parser: XmlParser): XmlSource[URI] = url.contramap(u => u.toURL)
}