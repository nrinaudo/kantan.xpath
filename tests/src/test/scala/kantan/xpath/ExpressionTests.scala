package kantan.xpath

import kantan.codecs.laws.CodecValue
import kantan.xpath.laws.discipline.arbitrary._
import kantan.xpath.ops._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import _root_.cats.Traverse.ops._
import _root_.cats.std.list._
import cats._

// TODO: these tests rely on CodeValue serializing values to a node named 'element'. This is fragile.
// TODO: Arbitrary[List[CodecValue[Node, A]]] never ends up generating lists of legal values only, which sorts of
//       defeats the purpose.
class ExpressionTests extends FunSuite with GeneratorDrivenPropertyChecks {
  type Value[A] = CodecValue[Node, A]

  def encodeAll[A](bs: List[Value[A]]): Element = {
    val n = bs.foldLeft("<root></root>".asNode.get.asInstanceOf[Document]) { (doc, b) ⇒
      val an = b.encoded.cloneNode(true)
      doc.adoptNode(an)
      doc.getFirstChild.appendChild(an)
      doc
    }
    n.getFirstChild.asInstanceOf[Element]
  }

  test("decodeFirst should fail on illegal values and succeed on legal ones") {
    forAll { value: Value[Int] ⇒
      assert("//element".xpath.first[Int](value.encoded) == NodeDecoder[Int].decode(value.encoded))
    }
  }

  test("decodeAll should fail on lists containing at least one illegal value and succeed on others") {
    forAll { values: List[Value[Int]] ⇒
      assert("//element".xpath.all[List, Int](encodeAll(values)) == values.map(v ⇒ NodeDecoder[Int].decode(v.encoded)).sequenceU)
    }
  }

  test("decodeEvery should always succeed, with failures for individual illegal nodes") {
    forAll { values: List[Value[Int]] ⇒
      assert("//element".xpath.every[List, Int](encodeAll(values)) == values.map(v ⇒ NodeDecoder[Int].decode(v.encoded)))
    }
  }

  test("liftFirst should fail on illegal values and succeed on legal ones") {
    forAll { value: Value[Int] ⇒
      val f ="//element".xpath.liftFirst[Int]
      assert(f(value.encoded) == NodeDecoder[Int].decode(value.encoded))
    }
  }

  test("liftAll should fail on lists containing at least one illegal value and succeed on others") {
    forAll { values: List[Value[Int]] ⇒
      val f ="//element".xpath.liftAll[List, Int]
      assert(f(encodeAll(values)) == values.map(v ⇒ NodeDecoder[Int].decode(v.encoded)).sequenceU)
    }
  }

  test("liftEvery should fail on illegal values and succeed on legal ones") {
    forAll { values: List[Value[Int]] ⇒
      val f ="//element".xpath.liftEvery[List, Int]
      assert(f(encodeAll(values)) == values.map(v ⇒ NodeDecoder[Int].decode(v.encoded)))
    }
  }
}
