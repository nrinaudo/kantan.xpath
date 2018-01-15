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
import kantan.codecs.Result
import kantan.codecs.laws.CodecValue
import laws.discipline.arbitrary._
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import scala.util.Try

class XmlSourceOpsTests extends FunSuite with GeneratorDrivenPropertyChecks with Matchers {
  type Value[A] = CodecValue[Node, A, codecs.type]

  private def cmp[A, F, E, T](value: CodecValue[E, A, T], res: Result[F, A]): Unit = (value, res) match {
    case (CodecValue.LegalValue(_, n1), Success(n2)) ⇒
      n1 should be(n2)
      ()
    case (CodecValue.IllegalValue(_), Failure(_)) ⇒ ()
    case (a, b)                                   ⇒ fail(s"$a is not compatible with $b")
  }

  private def cmp[A, F, E, T](value: CodecValue[E, A, T], res: Try[A]): Unit = cmp(value, Result.fromTry(res))

  test("XmlSource instances should have a working asNode method") {
    forAll { value: Value[Int] ⇒
      cmp(value, value.encoded.asNode.flatMap(_.evalXPath[Int](xp"/element")))
    }
  }

  // This test is not as good as it could be - we're not comparing decoded XML. The reason for that is that, apparently,
  // Node equality is not something the JDK deals with.
  test("XmlSource instances should have a working asUnsafeNode method") {
    forAll { value: CodecValue[String, Node, codecs.type] ⇒
      (value, Result.fromTry(Try(value.encoded.asUnsafeNode))) match {
        case (CodecValue.LegalValue(_, _), Success(_)) ⇒ true
        case (CodecValue.IllegalValue(_), Failure(_))  ⇒ true
        case (a, b)                                    ⇒ fail(s"$a is not compatible with $b")
      }
    }
  }

  test("XmlSource instances should have a working evalXPath(String) method") {
    forAll { value: Value[Int] ⇒
      cmp(value, value.encoded.evalXPath[Int](xp"/element"))
    }
  }

  test("XmlSource instances should have a working unsafeEvalXPath(String) method") {
    forAll { value: Value[Int] ⇒
      cmp(value, Try(value.encoded.unsafeEvalXPath[Int](xp"/element")))
    }
  }
}
