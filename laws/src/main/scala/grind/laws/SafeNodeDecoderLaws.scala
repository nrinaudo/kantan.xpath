package grind.laws

import grind._
import grind.ops._

trait SafeNodeDecoderLaws[A] {
  implicit def decoder: NodeDecoder[A]

  def encode(a: A, name: String): Element

  def encodeAll[B](bs: List[B], name: String)(f: (B, String) => Element): Element = {
    val n = bs.foldLeft("<root></root>".asNode.asInstanceOf[Document]) { (doc, b) =>
      val an = f(b, "e")
      doc.adoptNode(an)
      doc.getFirstChild.appendChild(an)
      doc
    }
    n.getFirstChild.asInstanceOf[Element]
  }

  def encodeAll(as: List[A], name: String): Element = encodeAll[A](as, name)(encode)

  def decodeFirst(a: A): Boolean = "//e".xpath.first[A](encode(a, "e")) == DecodeResult.Success(a)

  def decodeAll(as: List[A]): Boolean = "//e".xpath.all[List, A](encodeAll(as, "e")) == as.map(a => DecodeResult(a))

  def unsafeDecodeFirst(a: A): Boolean = "//e".xpath.unsafeFirst[A](encode(a, "e")) == a

  def unsafeDecodeAll(as: List[A]): Boolean = "//e".xpath.unsafeAll[List, A](encodeAll(as, "e")) == as

  def liftFirst(a: A): Boolean = "//e".xpath.liftFirst[A](decoder)(encode(a, "e")) == DecodeResult.Success(a)

  def liftAll(as: List[A]): Boolean = {
    val f = "//e".xpath.liftAll[List, A]
    f(encodeAll(as, "e")) == as.map(a => DecodeResult(a))
  }

  def liftUnsafeFirst(a: A): Boolean = "//e".xpath.liftUnsafeFirst[A](decoder)(encode(a, "e")) == a

  def liftUnsafeAll(as: List[A]): Boolean = {
    val f = "//e".xpath.liftUnsafeAll[List, A]
    f(encodeAll(as, "e")) == as
  }
}


object SafeNodeDecoderLaws {
  def apply[A](f: (A, String) => Element)(implicit da: NodeDecoder[A]): SafeNodeDecoderLaws[A] = new SafeNodeDecoderLaws[A] {
    override def encode(a: A, name: String) = f(a, name)
    override val decoder = da
  }
}
