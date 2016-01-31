package xerus

import javax.xml.xpath.XPathFactory

// TODO: error handling.
trait XPathCompiler {
  def compile(str: String): Expression
}

object XPathCompiler {
  def apply(f: String => Expression): XPathCompiler = new XPathCompiler {
    override def compile(str: String): Expression = f(str)
  }

  implicit val builtIn: XPathCompiler = {
    val cmp = XPathFactory.newInstance().newXPath()
    XPathCompiler(s => Expression(cmp.compile(s)))
  }
}
