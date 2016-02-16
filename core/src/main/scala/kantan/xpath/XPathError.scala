package kantan.xpath

sealed abstract class XPathError

object XPathError {
  // - XPath related errors --------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** Category of errors that occur while evaluating an XPath expression. */
  sealed abstract class EvaluationError extends XPathError
  case object NotFound extends EvaluationError
  final case class DecodeError(cause: Throwable) extends EvaluationError {
    override def equals(obj: Any) = obj match {
      case DecodeError(cause2) ⇒ cause.getClass == cause2.getClass
      case _                   ⇒ false
    }
  }



  // - XML related errors ----------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  /** Category of errors that occur while loading an XML document. */
  sealed abstract class LoadingError extends XPathError
  final case class ParseError(cause: Throwable) extends LoadingError {
    override def equals(obj: Any) = obj match {
      case ParseError(cause2) ⇒ cause.getClass == cause2.getClass
      case _                   ⇒ false
    }
  }
  final case class IOError(cause: Throwable) extends LoadingError {
    override def equals(obj: Any) = obj match {
      case IOError(cause2) ⇒ cause.getClass == cause2.getClass
      case _                   ⇒ false
    }
  }
}
