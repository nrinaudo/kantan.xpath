package grind.laws

import grind.ops._
import grind.{DecodeResult, Node, NodeDecoder}

trait NodeDecoderLaws[A] {
  implicit def decoder: NodeDecoder[A]

  def encode(a: A, name: String): Node

  def decodeFirst(value: A): Boolean = "//e".xpath.first[A](encode(value, "e")) == DecodeResult.Success(value)

  def unsafeDecodeFirst(value: A): Boolean = "//e".xpath.unsafeFirst[A](encode(value, "e")) == value

  def liftFirst(value: A): Boolean = "//e".xpath.liftFirst[A](decoder)(encode(value, "e")) == DecodeResult.Success(value)

  def liftUnsafeFirst(value: A): Boolean = "//e".xpath.liftUnsafeFirst[A](decoder)(encode(value, "e")) == value
}


object NodeDecoderLaws {
  def apply[A](f: (A, String) => Node)(implicit da: NodeDecoder[A]): NodeDecoderLaws[A] = new NodeDecoderLaws[A] {
    override def encode(a: A, name: String) = f(a, name)
    override val decoder = da
  }
}
