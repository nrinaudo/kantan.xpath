# grind
I find myself having to scrap some website or other with some regularity, and Scala always makes the whole process 
more painful than it really needs to be - the standard XML API is ok, I suppose, but the lack of XPath support
(or actually usable XPath-like DSL) is frustrating.

grind is a thin wrapper around the Java XPath API that attempts to be type safe, pleasant to use and hide the nasty
Java XML types whenever possible.
