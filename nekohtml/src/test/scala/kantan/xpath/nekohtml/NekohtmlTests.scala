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

package kantan.xpath.nekohtml

import kantan.xpath.implicits._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class NekohtmlTests extends AnyFunSuite with Matchers {
  test("The nekohtml parser should clean up metacritic data") {
    val letters = ('a' to 'z').map(c => s"/browse/games/title/ps2/$c").toList
    val result = getClass.getResource("/metacritic.html").evalXPath[List[String]](xp"//ul[@class='letternav']//a/@href")

    result should be(Right(letters))
  }
}
