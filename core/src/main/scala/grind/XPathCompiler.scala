package grind

import javax.xml.xpath.XPathFactory

import scala.util.Try

trait XPathCompiler {
  def compile(str: String): Option[Expression]
}

object XPathCompiler {
  def apply(f: String => Option[Expression]): XPathCompiler = new XPathCompiler {
    override def compile(str: String) = f(str)
  }

  def safe(f: String => Expression): XPathCompiler = XPathCompiler(s => Try(f(s)).toOption)

  implicit val builtIn: XPathCompiler = {
    val cmp = XPathFactory.newInstance().newXPath()
    safe(s => Expression(cmp.compile(s)))
  }
}
