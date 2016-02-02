package grind.laws

import grind.ops._
import grind._

trait NodeDecoderLaws[A] extends SafeNodeDecoderLaws[A] {
  def encodeFailure(value: String, name: String): Element
  def decodeFirstFail(ia: IllegalValue[A]): Boolean = "//e".xpath.first[A](encodeFailure(ia.str, "e")) == DecodeResult.Failure
}


object NodeDecoderLaws {
  def apply[A](f: (A, String) => Element)(g: (String, String) => Element)(implicit da: NodeDecoder[A]): NodeDecoderLaws[A] = new NodeDecoderLaws[A] {
    override def encode(a: A, name: String) = f(a, name)
    override def encodeFailure(value: String, name: String) = g(value, name)
    override val decoder = da
  }
}
