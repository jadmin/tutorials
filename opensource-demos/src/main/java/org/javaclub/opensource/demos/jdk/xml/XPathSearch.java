/*
 * @(#)XPathSearch.java	2011-8-26
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * XPathSearch
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: XPathSearch.java 2011-8-26 下午01:00:19 Exp $
 */
public class XPathSearch {

	public static void main(String[] args) throws Exception {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    Document retval = dbf.newDocumentBuilder().newDocument();
	    Element parent = retval.createElement("parent");
	    retval.appendChild(parent);

	    Element child1 = retval.createElement("child");
	    child1.setTextContent("child.text");
	    parent.appendChild(child1);
	    Element child2 = retval.createElement("child");
	    child2.setTextContent("child.text.2");
	    parent.appendChild(child2);

	    XPathFactory factory = XPathFactory.newInstance();
	    XPath xPath = factory.newXPath();
	    //Node evaluate = (Node) xPath.evaluate("//child/text()", retval, XPathConstants.NODE);
	    //Node evaluate = (Node) xPath.evaluate("//child[1]/text()", retval, XPathConstants.NODE);
	    Node evaluate = (Node) xPath.evaluate("//child[2]/text()", retval, XPathConstants.NODE);
		System.out.println(evaluate.getClass() + "\n" + evaluate.getTextContent());

	  }
}
