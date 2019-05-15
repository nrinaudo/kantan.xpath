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
package cats

import _root_.cats.Show
import _root_.cats.kernel.laws.discipline.EqTests
import laws.discipline._, arbitrary._

class ErrorTests extends DisciplineSuite {

  checkAll("XPathError", EqTests[XPathError].eqv)
  checkAll("CompileError", EqTests[CompileError].eqv)
  checkAll("ReadError", EqTests[ReadError].eqv)
  checkAll("DecodeError", EqTests[DecodeError].eqv)
  checkAll("DecodeError.NotFound", EqTests[DecodeError.NotFound.type].eqv)
  checkAll("DecodeError.TypeError", EqTests[DecodeError.TypeError].eqv)
  checkAll("ParseError", EqTests[ParseError].eqv)
  checkAll("ParseError.SyntaxError", EqTests[ParseError.SyntaxError].eqv)
  checkAll("ParseError.IOError", EqTests[ParseError.IOError].eqv)

  test("Show[CompileError] should yield a string containing the expected message") {
    forAll { error: CompileError =>
      Show[CompileError].show(error) should include(error.message)
      Show[XPathError].show(error) should include(error.message)
    }
  }

  test("Show[DecodeError.TypeError] should yield a string containing the expected message") {
    forAll { error: DecodeError.TypeError =>
      Show[DecodeError.TypeError].show(error) should include(error.message)
      Show[DecodeError].show(error) should include(error.message)
      Show[ReadError].show(error) should include(error.message)
      Show[XPathError].show(error) should include(error.message)
    }
  }

  test("Show[DecodeError.NotFound] should yield a string containing the expected message") {
    forAll { error: DecodeError.NotFound.type =>
      val expected = "no matched node"

      Show[DecodeError.NotFound.type].show(error) should include(expected)
      Show[DecodeError].show(error) should include(expected)
      Show[ReadError].show(error) should include(expected)
      Show[XPathError].show(error) should include(expected)
    }
  }

  test("Show[ParseError.SyntaxError] should yield a string containing the expected message") {

    forAll { error: ParseError.SyntaxError =>
      Show[ParseError.SyntaxError].show(error) should include(error.message)
      Show[ParseError].show(error) should include(error.message)
      Show[ReadError].show(error) should include(error.message)
      Show[XPathError].show(error) should include(error.message)
    }
  }

  test("Show[ParseError.IOError] should yield a string containing the expected message") {

    forAll { error: ParseError.IOError =>
      Show[ParseError.IOError].show(error) should include(error.message)
      Show[ParseError].show(error) should include(error.message)
      Show[ReadError].show(error) should include(error.message)
      Show[XPathError].show(error) should include(error.message)
    }
  }

}
