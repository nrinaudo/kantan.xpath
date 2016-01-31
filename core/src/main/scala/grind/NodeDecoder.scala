package grind

import java.net.{URI, URL}
import java.util.UUID

import simulacrum.{noop, typeclass}

@typeclass
trait NodeDecoder[A] { self =>
  def decode(n: Node): DecodeResult[A]

  @noop
  def map[B](f: A => B): NodeDecoder[B] = NodeDecoder { n => for {
    a <- self.decode(n)
    b <- DecodeResult(f(a))
  } yield b
  }
}

object NodeDecoder {
  def apply[A](f: Node => DecodeResult[A]) = new NodeDecoder[A] {
    override def decode(e: Node) = f(e)
  }

  def unsafe[A](f: Node => A): NodeDecoder[A] = new NodeDecoder[A] {
    override def decode(n: Node): DecodeResult[A] = DecodeResult(f(n))
  }

  implicit val node: NodeDecoder[Node] = unsafe(n => n)
  implicit val element: NodeDecoder[Element] = unsafe(n => n.asInstanceOf[Element])
  implicit val attr: NodeDecoder[Attr] = unsafe(n => n.asInstanceOf[Attr])
  implicit val string: NodeDecoder[String] = unsafe(_.getTextContent)
  implicit val char: NodeDecoder[Char] = string.map(s => if(s.length != 1) sys.error("todo") else s.charAt(0))
  implicit val int: NodeDecoder[Int] = string.map(_.toInt)
  implicit val float: NodeDecoder[Float] = string.map(_.toFloat)
  implicit val double: NodeDecoder[Double] = string.map(_.toDouble)
  implicit val long: NodeDecoder[Long] = string.map(_.toLong)
  implicit val short: NodeDecoder[Short] = string.map(_.toShort)
  implicit val byte: NodeDecoder[Byte] = string.map(_.toByte)
  implicit val boolean: NodeDecoder[Boolean] = string.map(_.toBoolean)
  implicit val bigInt: NodeDecoder[BigInt] = string.map(BigInt.apply)
  implicit val bigDecimal: NodeDecoder[BigDecimal] = string.map(BigDecimal.apply)
  implicit val uuid: NodeDecoder[UUID] = string.map(UUID.fromString)
  implicit val url: NodeDecoder[URL] = string.map(s => new URL(s))
  implicit val uri: NodeDecoder[URI] = string.map(s => new URI(s))
}
