package kantan.xpath

import java.io._
import java.net.{URI, URL}
import org.xml.sax.InputSource
import scala.collection.generic.CanBuildFrom
import scala.io.Codec
import simulacrum.noop

trait XmlSource[-A] extends Serializable { self ⇒
  def asNode(a: A): ParseResult

  def asUnsafeNode(a: A): Node = asNode(a).get

  def first[B: NodeDecoder](a: A, expr: Expression): ReadResult[B] = for {
    node ← asNode(a)
    b    ← expr.first[B](node)
  } yield b

  def all[F[_], B: NodeDecoder](a: A, expr: Expression)
                               (implicit cbf: CanBuildFrom[Nothing, B, F[B]]): ReadResult[F[B]] =
    for {
      node ← asNode(a)
      bs   ←  expr.all[F, B](node)
    } yield bs

  @noop
  def contramap[B](f: B ⇒ A): XmlSource[B] = XmlSource(b ⇒ self.asNode(f(b)))
}

object XmlSource {
  def apply[A](f: A ⇒ ParseResult): XmlSource[A] = new XmlSource[A] {
    override def asNode(a: A) = f(a)
  }

  implicit val node: XmlSource[Node] = XmlSource(n ⇒ ParseResult(n))

  implicit def inputSource(implicit parser: XmlParser): XmlSource[InputSource] =
    XmlSource(s ⇒ parser.parse(s))

  implicit def reader(implicit parser: XmlParser): XmlSource[Reader] = inputSource.contramap(r ⇒ new InputSource(r))
  implicit def inputStream(implicit codec: Codec, parser: XmlParser): XmlSource[InputStream] =
    reader.contramap(i ⇒ new InputStreamReader(i, codec.charSet))
  implicit def file(implicit codec: Codec, parser: XmlParser): XmlSource[File] =
    inputStream.contramap(f ⇒ new FileInputStream(f))
  implicit def string(implicit parser: XmlParser): XmlSource[String] = reader.contramap(s ⇒ new StringReader(s))
  implicit def url(implicit codec: Codec, parser: XmlParser): RemoteXmlSource[URL] = RemoteXmlSource(identity)
  implicit def uri(implicit codec: Codec, parser: XmlParser): RemoteXmlSource[URI] = url.contramap(_.toURL)
}
