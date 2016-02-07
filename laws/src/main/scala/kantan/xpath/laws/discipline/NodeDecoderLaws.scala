package kantan.xpath.laws.discipline

import kantan.xpath._
import kantan.xpath.ops._

trait NodeDecoderLaws[A] {
  implicit def decoder: NodeDecoder[A]

  def encode(a: A, name: String): Element


  // - Helper methods --------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  def encodeAll[B](bs: List[B], name: String)(f: (B, String) => Element): Element = {
    val n = bs.foldLeft("<root></root>".asNode.get.asInstanceOf[Document]) { (doc, b) =>
      val an = f(b, "e").cloneNode(true)
      doc.adoptNode(an)
      doc.getFirstChild.appendChild(an)
      doc
    }
    n.getFirstChild.asInstanceOf[Element]
  }

  def encodeAll(as: List[A], name: String): Element = encodeAll[A](as, name)(encode)



  // - Laws ------------------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  def decodeFirst(a: A): Boolean = "//e".xpath.first[A](encode(a, "e")) == DecodeResult.Success(a)

  def decodeAll(as: List[A]): Boolean = "//e".xpath.all[List, A](encodeAll(as, "e")) == DecodeResult.success(as)

  def decodeEvery(as: List[A]): Boolean = "//e".xpath.every[List, A](encodeAll(as, "e")) == as.map(a => DecodeResult(a))

  def liftFirst(a: A): Boolean = "//e".xpath.liftFirst[A](decoder)(encode(a, "e")) == DecodeResult.Success(a)

  def liftAll(as: List[A]): Boolean = {
    val f = "//e".xpath.liftAll[List, A]
    f(encodeAll(as, "e")) == DecodeResult.success(as)
  }

  def liftEvery(as: List[A]): Boolean = {
    val f = "//e".xpath.liftEvery[List, A]
    f(encodeAll(as, "e")) == as.map(a => DecodeResult.success(a))
  }
}


object NodeDecoderLaws {
  def apply[A](f: (A, String) => Element)(implicit da: NodeDecoder[A]): NodeDecoderLaws[A] = new NodeDecoderLaws[A] {
    override def encode(a: A, name: String) = f(a, name)
    override val decoder = da
  }
}
