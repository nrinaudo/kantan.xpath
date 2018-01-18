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

import implicits._
import kantan.codecs.laws.CodecValue
import laws.discipline.arbitrary._
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

// TODO: these tests rely on CodeValue serializing values to a node named 'element'. This is fragile.
// TODO: Arbitrary[List[CodecValue[Node, A]]] never ends up generating lists of legal values only, which sorts of
//       defeats the purpose.
class CompilerTests extends FunSuite with GeneratorDrivenPropertyChecks with Matchers {
  type Value[A] = CodecValue[Node, A, codecs.type]

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  def encodeAll[A](bs: List[Value[A]]): Element = {
    val n = bs.foldLeft("<root></root>".asNode.right.get.asInstanceOf[Document]) { (doc, b) ⇒
      val an = b.encoded.cloneNode(true)
      doc.adoptNode(an)
      doc.getFirstChild.appendChild(an)
      doc
    }
    n.getFirstChild.asInstanceOf[Element]
  }

  test("'first' expressions should fail on illegal values and succeed on legal ones") {
    forAll { value: Value[Int] ⇒
      value.encoded.evalXPath[Int](xp"//element") should be(NodeDecoder[Int].decode(Option(value.encoded)))
    }
  }

  test("'first' expressions should fail on empty results") {
    forAll { value: Value[Int] ⇒
      value.encoded.evalXPath[Int](xp"//element2") should be(DecodeResult.notFound)
    }
  }

  test("'all' expressions should fail on lists containing at least one illegal value and succeed on others") {
    forAll { values: List[Value[Int]] ⇒
      encodeAll(values).evalXPath[List[Int]](xp"//element") should be(
        DecodeResult.sequence(values.map(v ⇒ NodeDecoder[Int].decode(Option(v.encoded))))
      )
    }
  }
}
