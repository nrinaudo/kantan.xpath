package kantan.xpath

import kantan.xpath.DecodeResult._

sealed trait DecodeResult[+A] {
  def isSuccess: Boolean
  def isFailure: Boolean = !isSuccess
  def toOption: Option[A]

  def getOrElse[B >: A](a: => B): B = this match {
    case Success(v) => v
    case _          => a
  }

  def orElse[B >: A](result: => DecodeResult[B]): DecodeResult[B] =
    if(isSuccess) this
    else          result

  def get: A = this match {
    case Success(a) => a
    case _          => sys.error("get on non-success")
  }

  def map[B](f: A => B): DecodeResult[B] = this match {
    case Success(a) => Success(f(a))
    case Failure    => Failure
    case NotFound   => NotFound
  }

  def flatMap[B](f: A => DecodeResult[B]): DecodeResult[B] = this match {
    case Success(a) => f(a)
    case Failure    => Failure
    case NotFound   => NotFound
  }
}

object DecodeResult {
  def success[A](a: A): DecodeResult[A]  = Success(a)
  def failure[A]: DecodeResult[A] = Failure
  def notFound[A]: DecodeResult[A]  = NotFound

  def apply[A](a: => A): DecodeResult[A] =
    try {
      val actual = a
      if(actual == null) NotFound
      else               Success(actual)
    }
    catch { case _: Exception => Failure }

  final case class Success[A](value: A) extends DecodeResult[A] {
    override def isSuccess: Boolean = true
    override def toOption: Option[A] = Some(value)
  }

  case object Failure extends DecodeResult[Nothing] {
    override def isSuccess: Boolean = false
    override def toOption: Option[Nothing] = None
  }

  case object NotFound extends DecodeResult[Nothing] {
    override def isSuccess: Boolean = false
    override def toOption: Option[Nothing] = None
  }
}
