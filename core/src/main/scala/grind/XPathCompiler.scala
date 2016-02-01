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

  def unsafe(f: String => Expression): XPathCompiler = new XPathCompiler {
    override def compile(str: String): Option[Expression] = Try(f(str)).toOption
  }

  implicit val builtIn: XPathCompiler = {
    val cmp = XPathFactory.newInstance().newXPath()
    unsafe(s => Expression(cmp.compile(s)))
  }
}
