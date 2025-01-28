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

import javax.xml.namespace.QName
import javax.xml.xpath.XPathFactory

/** Compiles XPath expressions.
  *
  * There's always a [[XPathCompiler$.builtIn default instance]] in scope, which should be perfectly suitable for most
  * circumstances. Still, if some non-standard options are required, one can always declare a local implicit instance.
  *
  * Be careful, however: the default compiler performs some tricks to make sure its results are serializable. If you're
  * planning on using frameworks that require serialization, such as Apache Spark, think twice about using a non-default
  * compiler.
  */
trait XPathCompiler extends Serializable {
  def compile(str: String): CompileResult[XPathExpression]
}

object XPathCompiler {

  /** Creates a new [[XPathCompiler]] from the specified function. */
  def apply(f: String => CompileResult[XPathExpression]): XPathCompiler =
    new XPathCompiler {
      override def compile(str: String) =
        f(str)
    }

  /** Default compiler, always in scope. */
  @SuppressWarnings(Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Serializable"))
  implicit val builtIn: XPathCompiler = XPathCompiler { str =>
    CompileResult(XPathFactory.newInstance().newXPath().compile(str)).map { _ =>
      new XPathExpression with Serializable {
        private val expression: String       = str
        @transient private lazy val compiled = XPathFactory.newInstance().newXPath().compile(expression)

        override def evaluate(item: scala.Any, returnType: QName) =
          compiled.evaluate(item, returnType)
        override def evaluate(item: scala.Any) =
          compiled.evaluate(item)
        override def evaluate(source: InputSource, returnType: QName) =
          compiled.evaluate(source, returnType)
        override def evaluate(source: InputSource) =
          compiled.evaluate(source)
      }
    }
  }
}
