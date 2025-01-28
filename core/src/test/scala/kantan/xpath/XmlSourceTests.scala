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

import kantan.xpath.implicits._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.nio.file.Files

class XmlSourceTests extends AnyFunSuite with Matchers {

  test("XmlSource[File] should correctly handle XML encoding") {
    val path = Files.createTempFile("kx", ".xml")
    val data1251 = """
      <?xml version="1.0" encoding="windows-1251"?>
      <abc>Проверка 1251</abc>
    """.trim
    Files.write(path, data1251.getBytes("windows-1251"))
    path.evalXPath[String](xp"//abc") should be(Right("Проверка 1251"))
  }
}
