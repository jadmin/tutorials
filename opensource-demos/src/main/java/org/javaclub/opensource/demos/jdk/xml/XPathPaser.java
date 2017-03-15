/*
 * @(#)XPathPaser.java	2011-8-26
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.xml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * XPathPaser
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: XPathPaser.java 2011-8-26 下午01:57:49 Exp $
 */
public class XPathPaser {
	public static void main(String[] args) throws Exception {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String xpathExpression = "//constant/@name";
		InputSource inputSource = new InputSource("D:/tmp/jdbc.cfg.xml");

		NodeList nodes = (NodeList) xpath.evaluate(xpathExpression,
				inputSource, XPathConstants.NODESET);

		int j = nodes.getLength();

		for (int i = 0; i < j; i++) {
			System.out.println(nodes.item(i).getTextContent());
		}

	}
	
	public void test() throws Exception {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String xpathExpression = "//constant";
		InputSource inputSource = new InputSource("D:/tmp/jdbc.cfg.xml");

		NodeList nodes = (NodeList) xpath.evaluate(xpathExpression,
				inputSource, XPathConstants.NODESET);
		
		Node node = null;
		for (int i = 0; i < nodes.getLength(); i++) {
			NamedNodeMap map = node.getAttributes();
			node = map.getNamedItem("name");
			System.out.println(node.getTextContent());
		}
	}
}
