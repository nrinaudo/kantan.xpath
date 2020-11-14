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

package kantan.xpath.scalaz

import kantan.codecs.scalaz.laws.discipline.ScalazDisciplineSuite
import kantan.xpath.{CompileError, DecodeError, ParseError, ReadError, XPathError}
import kantan.xpath.scalaz.arbitrary._
import scalaz.Show
import scalaz.scalacheck.ScalazProperties.{equal => equ}

class ErrorTests extends ScalazDisciplineSuite {

  checkAll("XPathError", equ.laws[XPathError])
  checkAll("CompileError", equ.laws[CompileError])
  checkAll("ReadError", equ.laws[ReadError])
  checkAll("DecodeError", equ.laws[DecodeError])
  checkAll("DecodeError.NotFound", equ.laws[DecodeError.NotFound.type])
  checkAll("DecodeError.TypeError", equ.laws[DecodeError.TypeError])
  checkAll("ParseError", equ.laws[ParseError])
  checkAll("ParseError.SyntaxError", equ.laws[ParseError.SyntaxError])
  checkAll("ParseError.IOError", equ.laws[ParseError.IOError])

  test("Show[CompileError] should yield a string containing the expected message") {
    forAll { error: CompileError =>
      Show[CompileError].shows(error) should include(error.message)
      Show[XPathError].shows(error) should include(error.message)
    }
  }

  test("Show[DecodeError.TypeError] should yield a string containing the expected message") {
    forAll { error: DecodeError.TypeError =>
      Show[DecodeError.TypeError].shows(error) should include(error.message)
      Show[DecodeError].shows(error) should include(error.message)
      Show[ReadError].shows(error) should include(error.message)
      Show[XPathError].shows(error) should include(error.message)
    }
  }

  test("Show[DecodeError.NotFound] should yield a string containing the expected message") {
    forAll { error: DecodeError.NotFound.type =>
      val expected = "no matched node"

      Show[DecodeError.NotFound.type].shows(error) should include(expected)
      Show[DecodeError].shows(error) should include(expected)
      Show[ReadError].shows(error) should include(expected)
      Show[XPathError].shows(error) should include(expected)
    }
  }

  test("Show[ParseError.SyntaxError] should yield a string containing the expected message") {

    forAll { error: ParseError.SyntaxError =>
      Show[ParseError.SyntaxError].shows(error) should include(error.message)
      Show[ParseError].shows(error) should include(error.message)
      Show[ReadError].shows(error) should include(error.message)
      Show[XPathError].shows(error) should include(error.message)
    }
  }

  test("Show[ParseError.IOError] should yield a string containing the expected message") {

    forAll { error: ParseError.IOError =>
      Show[ParseError.IOError].shows(error) should include(error.message)
      Show[ParseError].shows(error) should include(error.message)
      Show[ReadError].shows(error) should include(error.message)
      Show[XPathError].shows(error) should include(error.message)
    }
  }

}
