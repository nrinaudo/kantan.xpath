package kantan.xpath

import java.net.{URI, URL}
import java.util.UUID

import simulacrum.{noop, typeclass}

@typeclass
trait NodeDecoder[A] { self ⇒
  def decode(n: Node): EvaluationResult[A]

  @noop
  def map[B](f: A ⇒ B): NodeDecoder[B] = NodeDecoder { n ⇒ self.decode(n).map(f) }

  @noop
  def flatMap[B](f: A ⇒ NodeDecoder[B]): NodeDecoder[B] = NodeDecoder(s ⇒ decode(s).flatMap(a ⇒ f(a).decode(s)))

  @noop
  def mapResult[B](f: A ⇒ EvaluationResult[B]): NodeDecoder[B] = NodeDecoder { n: Node ⇒ self.decode(n).flatMap(f) }
}

@export.imports[NodeDecoder]
trait LowPriorityNodeDecoders

object NodeDecoder extends LowPriorityNodeDecoders with Decoders with TupleDecoders {
  def apply[A](f: Node ⇒ EvaluationResult[A]) = new NodeDecoder[A] {
    override def decode(e: Node) = f(e)
  }

  def safe[A](f: Node ⇒ A): NodeDecoder[A] = new NodeDecoder[A] {
    override def decode(n: Node): EvaluationResult[A] = EvaluationResult.apply(f(n))
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
  implicit val int: NodeDecoder[Int] = string.mapResult(s ⇒ EvaluationResult.apply(s.trim.toInt))
  implicit val float: NodeDecoder[Float] = string.mapResult(s ⇒ EvaluationResult.apply(s.trim.toFloat))
  implicit val double: NodeDecoder[Double] = string.mapResult(s ⇒ EvaluationResult.apply(s.trim.toDouble))
  implicit val long: NodeDecoder[Long] = string.mapResult(s ⇒ EvaluationResult.apply(s.trim.toLong))
  implicit val short: NodeDecoder[Short] = string.mapResult(s ⇒ EvaluationResult.apply(s.trim.toShort))
  implicit val byte: NodeDecoder[Byte] = string.mapResult(s ⇒ EvaluationResult.apply(s.trim.toByte))
  implicit val boolean: NodeDecoder[Boolean] = string.mapResult(s ⇒ EvaluationResult.apply(s.trim.toBoolean))
  implicit val bigInt: NodeDecoder[BigInt] = string.mapResult(s ⇒ EvaluationResult.apply(BigInt(s.trim)))
  implicit val bigDecimal: NodeDecoder[BigDecimal] = string.mapResult(s ⇒ EvaluationResult.apply(BigDecimal(s.trim)))
  implicit val uuid: NodeDecoder[UUID] = string.mapResult(s ⇒ EvaluationResult.apply(UUID.fromString(s.trim)))
  implicit val uri: NodeDecoder[URI] = string.mapResult(s ⇒ EvaluationResult.apply(new URI(s.trim)))
  implicit val url: NodeDecoder[URL] = uri.map(u ⇒ u.toURL)

  implicit def either[A, B](implicit da: NodeDecoder[A], db: NodeDecoder[B]): NodeDecoder[Either[A, B]] = NodeDecoder { node ⇒
    da.decode(node).map(a ⇒ Left(a)).orElse(db.decode(node).map(b ⇒ Right(b)))
  }
}
