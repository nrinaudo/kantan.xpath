package kantan

import kantan.codecs.DecodeResult

package object xpath {
  // - Result types ----------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  type XPathResult[A] = DecodeResult[XPathError, A]
  type EvaluationResult[A] = DecodeResult[XPathError.EvaluationError, A]
  type LoadingResult = DecodeResult[XPathError.LoadingError, Node]


  // - XML types -------------------------------------------------------------------------------------------------------
  // -------------------------------------------------------------------------------------------------------------------
  type Element = org.w3c.dom.Element
  type Node = org.w3c.dom.Node
  type Document = org.w3c.dom.Document
  type Attr = org.w3c.dom.Attr
  type NodeList = org.w3c.dom.NodeList
}
