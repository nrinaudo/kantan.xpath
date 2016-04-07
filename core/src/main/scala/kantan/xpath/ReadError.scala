package kantan.xpath

sealed abstract class ReadError extends Product with Serializable

sealed abstract class DecodeError extends ReadError

object DecodeError {
  case object NotFound extends DecodeError
  final case class TypeError(cause: Throwable) extends DecodeError {
    override def toString: String = s"TypeError(${cause.getMessage})"

    override def equals(obj: Any) = obj match {
      case TypeError(cause2) ⇒ cause.getClass == cause2.getClass
      case _                 ⇒ false
    }

    override def hashCode(): Int = cause.hashCode()
  }
}

sealed abstract class ParseError extends ReadError

object ParseError {
  final case class SyntaxError(cause: Throwable) extends ParseError {
    override def toString: String = s"SyntaxError(${cause.getMessage})"

    override def equals(obj: Any) = obj match {
      case SyntaxError(cause2) ⇒ cause.getClass == cause2.getClass
      case _                   ⇒ false
    }

    override def hashCode(): Int = cause.hashCode()
  }
  final case class IOError(cause: Throwable) extends ParseError {
    override def toString: String = s"IOError(${cause.getMessage})"

    override def equals(obj: Any) = obj match {
      case IOError(cause2) ⇒ cause.getClass == cause2.getClass
      case _                   ⇒ false
    }

    override def hashCode(): Int = cause.hashCode()
  }
}
