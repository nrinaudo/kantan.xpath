package grind

import java.io._
import java.net.{URI, URL}

import org.xml.sax.InputSource
import simulacrum.{op, noop, typeclass}

import scala.collection.generic.CanBuildFrom
import scala.io.Codec

@typeclass
trait XmlSource[-A] { self =>
  def asNode(a: A): Node

  @noop
  def contramap[B](f: B => A): XmlSource[B] = XmlSource(b => self.asNode(f(b)))

  @op("evalFirst")
  def first[B: NodeDecoder](a: A, xpath: Expression): DecodeResult[B] = xpath.first(asNode(a))

  @op("unsafeEvalFirst")
  def unsafeFirst[B: NodeDecoder](a: A, xpath: Expression): B = xpath.unsafeFirst(asNode(a))

  @op("evalAll")
  def all[F[_], B: NodeDecoder](a: A, xpath: Expression)(implicit cbf: CanBuildFrom[Nothing, DecodeResult[B], F[DecodeResult[B]]]): F[DecodeResult[B]] =
    xpath.all(asNode(a))

  @op("unsafeEvalAll")
  def unsafeAll[F[_], B: NodeDecoder](a: A, xpath: Expression)(implicit cbf: CanBuildFrom[Nothing, B, F[B]]): F[B] =
    xpath.unsafeAll(asNode(a))
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