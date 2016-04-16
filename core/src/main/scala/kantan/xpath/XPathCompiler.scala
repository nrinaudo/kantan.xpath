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

import javax.xml.xpath.{XPathExpression, XPathFactory}
import kantan.codecs.Result

trait XPathCompiler {
  def compile(str: String): XPathResult[XPathExpression]
}

object XPathCompiler {
  def apply(f: String ⇒ XPathResult[XPathExpression]): XPathCompiler = new XPathCompiler {
    override def compile(str: String) = f(str)
  }

  implicit val builtInt: XPathCompiler = {
    val xpath = XPathFactory.newInstance().newXPath()
    XPathCompiler(str ⇒ Result.nonFatal(xpath.compile(str)).leftMap(CompileError.apply))
  }
}
