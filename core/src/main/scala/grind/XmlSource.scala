package grind

import java.io._
import java.net.{URI, URL}

import org.xml.sax.InputSource
import simulacrum.{op, noop, typeclass}

import scala.collection.generic.CanBuildFrom
import scala.io.Codec
import scala.util.Try

@typeclass
trait XmlSource[-A] { self =>
  def asNode(a: A): Option[Node]

  def getAsNode(a: A): Node = asNode(a).get

  @noop
  def contramap[B](f: B => A): XmlSource[B] = XmlSource(b => self.asNode(f(b)))

  @op("evalFirst")
  def first[B: NodeDecoder](a: A, xpath: Expression): DecodeResult[B] =
    asNode(a).fold(DecodeResult.failure[B])(n => xpath.first(n))

  @op("evalFirst")
  def first[B: NodeDecoder](a: A, xpath: UnsafeExpression): B =
    xpath.first(asNode(a).get)

  @op("evalAll")
  def all[F[_], B: NodeDecoder](a: A, xpath: Expression)(implicit cbf: CanBuildFrom[Nothing, DecodeResult[B], F[DecodeResult[B]]]): F[DecodeResult[B]] =
    asNode(a).fold(cbf().result())(n => xpath.all(n))

  @op("evalAll")
  def all[F[_], B: NodeDecoder](a: A, xpath: UnsafeExpression)(implicit cbf: CanBuildFrom[Nothing, B, F[B]]): F[B] =
    asNode(a).fold(cbf().result())(n => xpath.all(n))
}

object XmlSource {
  def apply[A](f: A => Option[Node]): XmlSource[A] = new XmlSource[A] {
    override def asNode(a: A) = f(a)
  }

  /** Construction method for types that cannot fail to parse as a `Node`. */
  def safe[A](f: A => Node): XmlSource[A] = XmlSource(a => Some(f(a)))

  implicit val node: XmlSource[Node] = XmlSource.safe(n => n)

  implicit def inputSource(implicit parser: XmlParser): XmlSource[InputSource] =
    XmlSource(s => Try(parser.parse(s)).toOption)

  implicit def reader(implicit parser: XmlParser): XmlSource[Reader] = inputSource.contramap(r => new InputSource(r))
  implicit def inputStream(implicit codec: Codec, parser: XmlParser): XmlSource[InputStream] = reader.contramap(i => new InputStreamReader(i, codec.charSet))
  implicit def file(implicit codec: Codec, parser: XmlParser): XmlSource[File] = inputStream.contramap(f => new FileInputStream(f))
  implicit def string(implicit parser: XmlParser): XmlSource[String] = reader.contramap(s => new StringReader(s))
  implicit def url(implicit codec: Codec, parser: XmlParser): XmlSource[URL] = inputStream.contramap(u => u.openStream())
  implicit def uri(implicit codec: Codec, parser: XmlParser): XmlSource[URI] = url.contramap(u => u.toURL)
}