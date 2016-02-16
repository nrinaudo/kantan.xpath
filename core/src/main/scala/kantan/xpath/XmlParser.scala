package kantan.xpath

import javax.xml.parsers.DocumentBuilderFactory

import org.xml.sax.InputSource

/** Contract for anything that knows how to parse XML.
  *
  * The default implementation is always in scope and uses the standard java `javax.xml.parsers.DocumentBuilderFactory`,
  * with no special option. This can be overridden to set some options or, more drastically, to change the parsing
  * mechanism entirely. A typical use case is the `nekohtml` module, which needs to take control of the entire parsing
  * process to tidy up messy HTML documents.
  */
trait XmlParser {
  /** Turns the specified `InputSource` into a `Document`. */
  def parse(source: InputSource): LoadingResult
}

/** Declares the default [[XmlParser]] instance in the implicit scope. */
object XmlParser {
  /** Helper creation method, turns the specified function into an `Xmlparser`. */
  def apply(f: InputSource ⇒ LoadingResult): XmlParser = new XmlParser {
    override def parse(source: InputSource) = f(source)
  }

  /** Default implementation.
    *
    * This relies on the standard java `javax.xml.parsers.DocumentBuilderFactory`, using whatever default parameters
    * were set.
    */
  implicit val builtIn: XmlParser = {
    val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    XmlParser(source ⇒ LoadingResult(factory.newDocumentBuilder().parse(source)))
  }
}
