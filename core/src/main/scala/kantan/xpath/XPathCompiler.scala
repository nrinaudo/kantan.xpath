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

import javax.xml.xpath.XPathFactory
import kantan.codecs.Result

/** Compiles XPath expressions.
  *
  * There's always a [[XPathCompiler$.builtIn default instance]] in scope, which should be perfectly suitable for
  * most circumstances. Still, if some non-standard options are required, one can always declare a local implicit
  * instance.
  */
trait XPathCompiler {
  def compile(str: String): CompileResult[XPathExpression]
}

object XPathCompiler {
  /** Creates a new [[XPathCompiler]] from the specified function. */
  def apply(f: String ⇒ CompileResult[XPathExpression]): XPathCompiler = new XPathCompiler {
    override def compile(str: String) = f(str)
  }

  /** Default compiler, always in scope. */
  implicit val builtIn: XPathCompiler = {
    val xpath = XPathFactory.newInstance().newXPath()
    XPathCompiler(str ⇒ Result.nonFatal(xpath.compile(str)).leftMap(CompileError.apply))
  }
}
