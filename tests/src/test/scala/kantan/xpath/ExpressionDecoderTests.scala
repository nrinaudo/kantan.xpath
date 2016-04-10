/*
 * Copyright 2016 Nicolas Rinaudo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kantan.xpath

import _root_.cats.std.list._
import _root_.cats.Traverse.ops._
import cats._
import kantan.codecs.laws.CodecValue
import kantan.xpath.laws.discipline.arbitrary._
import kantan.xpath.ops._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks

// TODO: these tests rely on CodeValue serializing values to a node named 'element'. This is fragile.
// TODO: Arbitrary[List[CodecValue[Node, A]]] never ends up generating lists of legal values only, which sorts of
//       defeats the purpose.
class ExpressionDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks {
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
      assert("//element".xpath.all[List, Int](encodeAll(values)) ==
             values.map(v ⇒ NodeDecoder[Int].decode(v.encoded)).sequenceU)
    }
  }

  test("decodeEvery should always succeed, with failures for individual illegal nodes") {
    forAll { values: List[Value[Int]] ⇒
      assert("//element".xpath.every[List, Int](encodeAll(values)) ==
             values.map(v ⇒ NodeDecoder[Int].decode(v.encoded)))
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
