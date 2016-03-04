package kantan

import kantan.codecs.{Decoder, Result}
import kantan.xpath.XPathError.EvaluationError

package object xpath {
  type NodeDecoder[A] = Decoder[Node, A, EvaluationError, Codecs.type]

  // - Result types ----------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  type XPathResult[A] = Result[XPathError, A]
  type EvaluationResult[A] = Result[XPathError.EvaluationError, A]
  type LoadingResult = Result[XPathError.LoadingError, Node]


  // - XML types -------------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  type Element = org.w3c.dom.Element
  type Node = org.w3c.dom.Node
  type Document = org.w3c.dom.Document
  type Attr = org.w3c.dom.Attr
  type NodeList = org.w3c.dom.NodeList
}
