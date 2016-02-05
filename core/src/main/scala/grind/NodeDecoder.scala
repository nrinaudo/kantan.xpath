package grind

import java.net.{URI, URL}
import java.util.UUID

import grind.DecodeResult.{Failure, Success}
import simulacrum.{noop, typeclass}

@typeclass
trait NodeDecoder[A] { self =>
  def decode(n: Node): DecodeResult[A]

  @noop
  def map[B](f: A => B): NodeDecoder[B] = NodeDecoder { n => self.decode(n).map(f) }

  @noop
  def flatMap[B](f: A => NodeDecoder[B]): NodeDecoder[B] = NodeDecoder(s => decode(s).flatMap(a => f(a).decode(s)))

  @noop
  def mapResult[B](f: A => DecodeResult[B]): NodeDecoder[B] = NodeDecoder { n => self.decode(n).flatMap(f) }
}

object NodeDecoder extends Decoders with TupleDecoders {
  def apply[A](f: Node => DecodeResult[A]) = new NodeDecoder[A] {
    override def decode(e: Node) = f(e)
  }

  def safe[A](f: Node => A): NodeDecoder[A] = new NodeDecoder[A] {
    override def decode(n: Node): DecodeResult[A] = DecodeResult(f(n))
  }

  implicit val node: NodeDecoder[Node] = safe(n => n)
  implicit val element: NodeDecoder[Element] = safe(n => n.asInstanceOf[Element])
  implicit val attr: NodeDecoder[Attr] = safe(n => n.asInstanceOf[Attr])
  implicit val string: NodeDecoder[String] = safe(_.getTextContent.trim)
  implicit val char: NodeDecoder[Char] = string.mapResult(s => if(s.length == 1) Success(s.charAt(0)) else Failure)
  implicit val int: NodeDecoder[Int] = string.mapResult(s => DecodeResult(s.toInt))
  implicit val float: NodeDecoder[Float] = string.mapResult(s => DecodeResult(s.toFloat))
  implicit val double: NodeDecoder[Double] = string.mapResult(s => DecodeResult(s.toDouble))
  implicit val long: NodeDecoder[Long] = string.mapResult(s => DecodeResult(s.toLong))
  implicit val short: NodeDecoder[Short] = string.mapResult(s => DecodeResult(s.toShort))
  implicit val byte: NodeDecoder[Byte] = string.mapResult(s => DecodeResult(s.toByte))
  implicit val boolean: NodeDecoder[Boolean] = string.mapResult(s => DecodeResult(s.toBoolean))
  implicit val bigInt: NodeDecoder[BigInt] = string.mapResult(s => DecodeResult(BigInt(s)))
  implicit val bigDecimal: NodeDecoder[BigDecimal] = string.mapResult(s => DecodeResult(BigDecimal(s)))
  implicit val uuid: NodeDecoder[UUID] = string.mapResult(s => DecodeResult(UUID.fromString(s)))
  implicit val uri: NodeDecoder[URI] = string.mapResult(s => DecodeResult(new URI(s)))
  implicit val url: NodeDecoder[URL] = uri.map(u => u.toURL)
}
