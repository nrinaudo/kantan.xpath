package grind.laws

import org.scalacheck.Prop._
import grind.ops._
import grind._

trait NodeDecoderLaws[A] extends SafeNodeDecoderLaws[A] {
  def encodeFailure(value: String, name: String): Element

  def decodeFirstFail(ia: IllegalValue[A]): Boolean =
    "//e".xpath.first[A](encodeFailure(ia.str, "e")) == DecodeResult.Failure

  def unsafeDecodeFirstFail(ia: IllegalValue[A]): Boolean =
    throws(classOf[java.lang.Exception])("//e".xpath.unsafeFirst[A](encodeFailure(ia.str, "e")))

  def liftFirstFail(ia: IllegalValue[A]): Boolean =
    "//e".xpath.liftFirst[A](decoder)(encodeFailure(ia.str, "e")) == DecodeResult.Failure

  def liftUnsafeFirstFail(ia: IllegalValue[A]): Boolean =
    throws(classOf[java.lang.Exception])("//e".xpath.liftUnsafeFirst[A](decoder)(encodeFailure(ia.str, "e")))

  def decodeAllFail(ias: List[IllegalValue[A]]): Boolean =
    "//e".xpath.all[List, A](encodeAll[String](ias.map(_.str), "e")(encodeFailure)) == ias.map(_ => DecodeResult.Failure)

  def unsafeDecodeAllFail(ias: List[IllegalValue[A]]): Boolean =
    if(ias.isEmpty) true
    else throws(classOf[java.lang.Exception])("//e".xpath.unsafeAll[List, A](encodeAll[String](ias.map(_.str), "e")(encodeFailure)))

  def liftAllFail(ias: List[IllegalValue[A]]): Boolean = {
    val f = "//e".xpath.liftAll[List, A]

    f(encodeAll[String](ias.map(_.str), "e")(encodeFailure)) == ias.map(_ => DecodeResult.Failure)
  }

  def liftUnsafeAllFail(ias: List[IllegalValue[A]]): Boolean = {
    val f = "//e".xpath.liftUnsafeAll[List, A]

    if(ias.isEmpty) true
    else throws(classOf[java.lang.Exception])(f(encodeAll[String](ias.map(_.str), "e")(encodeFailure)))
  }
}


object NodeDecoderLaws {
  def apply[A](f: (A, String) => Element)(g: (String, String) => Element)(implicit da: NodeDecoder[A]): NodeDecoderLaws[A] = new NodeDecoderLaws[A] {
    override def encode(a: A, name: String) = f(a, name)
    override def encodeFailure(value: String, name: String) = g(value, name)
    override val decoder = da
  }
}
