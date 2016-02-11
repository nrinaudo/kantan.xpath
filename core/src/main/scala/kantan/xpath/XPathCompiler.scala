package kantan.xpath

import javax.xml.xpath.XPathFactory

import scala.util.Try

trait XPathCompiler {
  def compile(str: String): Option[Expression]
}

object XPathCompiler {
  def apply(f: String ⇒ Option[Expression]): XPathCompiler = new XPathCompiler {
    override def compile(str: String) = f(str)
  }

  implicit val builtIn: XPathCompiler = {
    val cmp = XPathFactory.newInstance().newXPath()
    XPathCompiler(s ⇒ Try(Expression(cmp.compile(s))).toOption)
  }
}
