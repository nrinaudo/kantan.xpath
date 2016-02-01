package grind.laws

import grind.ops._
import grind._

trait NodeDecoderLaws[A] {
  implicit def decoder: NodeDecoder[A]

  def encode(a: A, name: String): Element

  def encode(as: List[A], name: String): Element = {
    val n = as.foldLeft("<root></root>".asNode.asInstanceOf[Document]) { (doc, a) =>
      val an = encode(a, "e")
      doc.adoptNode(an)
      doc.getFirstChild.appendChild(an)
      doc
    }
    n.getFirstChild.asInstanceOf[Element]
  }

  def decodeFirst(a: A): Boolean = "//e".xpath.first[A](encode(a, "e")) == DecodeResult.Success(a)

  def decodeAll(as: List[A]): Boolean = "//e".xpath.all[List, A](encode(as, "e")) == as.map(a => DecodeResult(a))

  def unsafeDecodeFirst(a: A): Boolean = "//e".xpath.unsafeFirst[A](encode(a, "e")) == a

  def unsafeDecodeAll(as: List[A]): Boolean = "//e".xpath.unsafeAll[List, A](encode(as, "e")) == as

  def liftFirst(a: A): Boolean = "//e".xpath.liftFirst[A](decoder)(encode(a, "e")) == DecodeResult.Success(a)

  def liftAll(as: List[A]): Boolean = {
    val f = "//e".xpath.liftAll[List, A]
    f(encode(as, "e")) == as.map(a => DecodeResult(a))
  }

  def liftUnsafeFirst(a: A): Boolean = "//e".xpath.liftUnsafeFirst[A](decoder)(encode(a, "e")) == a

  def liftUnsafeAll(as: List[A]): Boolean = {
    val f = "//e".xpath.liftUnsafeAll[List, A]
    f(encode(as, "e")) == as
  }
}


object NodeDecoderLaws {
  def apply[A](f: (A, String) => Element)(implicit da: NodeDecoder[A]): NodeDecoderLaws[A] = new NodeDecoderLaws[A] {
    override def encode(a: A, name: String) = f(a, name)
    override val decoder = da
  }
}
