package kantan.xpath

import java.net.{URI, URL}
import java.util.UUID

import kantan.codecs.Decoder
import simulacrum.typeclass

@typeclass
trait NodeDecoder[A] extends Decoder[Node, A, XPathError.EvaluationError, NodeDecoder] { self ⇒
  override def decode(n: Node): EvaluationResult[A]
  override protected def copy[DD](f: Node => EvaluationResult[DD]) = NodeDecoder(f)
}

@export.imports[NodeDecoder]
trait LowPriorityNodeDecoders

object NodeDecoder extends LowPriorityNodeDecoders with Decoders with TupleDecoders {
  def apply[A](f: Node ⇒ EvaluationResult[A]) = new NodeDecoder[A] {
    override def decode(e: Node) = f(e)
  }

  def safe[A](f: Node ⇒ A): NodeDecoder[A] = new NodeDecoder[A] {
    override def decode(n: Node): EvaluationResult[A] = EvaluationResult(f(n))
  }

  implicit val node: NodeDecoder[Node] = safe(n ⇒ n)
  implicit val element: NodeDecoder[Element] = safe(n ⇒ n.asInstanceOf[Element])
  implicit val attr: NodeDecoder[Attr] = safe(n ⇒ n.asInstanceOf[Attr])
  implicit val string: NodeDecoder[String] = safe(_.getTextContent)
  implicit val char: NodeDecoder[Char] = string.mapResult { s ⇒
    EvaluationResult {
      if(s.length != 1) throw new IllegalArgumentException(s"Not a valid character: '$s'")
      else              s.charAt(0)
    }
  }
  implicit val int: NodeDecoder[Int] = string.mapResult(s ⇒ EvaluationResult(s.trim.toInt))
  implicit val float: NodeDecoder[Float] = string.mapResult(s ⇒ EvaluationResult(s.trim.toFloat))
  implicit val double: NodeDecoder[Double] = string.mapResult(s ⇒ EvaluationResult(s.trim.toDouble))
  implicit val long: NodeDecoder[Long] = string.mapResult(s ⇒ EvaluationResult(s.trim.toLong))
  implicit val short: NodeDecoder[Short] = string.mapResult(s ⇒ EvaluationResult(s.trim.toShort))
  implicit val byte: NodeDecoder[Byte] = string.mapResult(s ⇒ EvaluationResult(s.trim.toByte))
  implicit val boolean: NodeDecoder[Boolean] = string.mapResult(s ⇒ EvaluationResult(s.trim.toBoolean))
  implicit val bigInt: NodeDecoder[BigInt] = string.mapResult(s ⇒ EvaluationResult(BigInt(s.trim)))
  implicit val bigDecimal: NodeDecoder[BigDecimal] = string.mapResult(s ⇒ EvaluationResult(BigDecimal(s.trim)))
  implicit val uuid: NodeDecoder[UUID] = string.mapResult(s ⇒ EvaluationResult(UUID.fromString(s.trim)))
  implicit val uri: NodeDecoder[URI] = string.mapResult(s ⇒ EvaluationResult(new URI(s.trim)))
  implicit val url: NodeDecoder[URL] = uri.map(u ⇒ u.toURL)

  implicit def either[A, B](implicit da: NodeDecoder[A], db: NodeDecoder[B]): NodeDecoder[Either[A, B]] = NodeDecoder { node ⇒
    da.decode(node).map(a ⇒ Left(a)).orElse(db.decode(node).map(b ⇒ Right(b)))
  }
}
