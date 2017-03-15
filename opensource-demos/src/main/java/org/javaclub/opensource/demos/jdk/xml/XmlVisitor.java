/*
 * @(#)XmlVisitor.java	2011-8-26
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XmlVisitor
 * 
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: XmlVisitor.java 2011-8-26 上午10:35:52 Exp $
 */
public class XmlVisitor {
	
	static String path = "D:/gerald/workspace/java/opensource/javaclub/gerald-jorm/src/test/resources/conf/jdbc.cfg.xml";

	public static void main(String[] argv) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// factory.setValidating(true);

		factory.setExpandEntityReferences(false);

		Document doc = factory.newDocumentBuilder().parse(new File(path));

		visit(doc, 0);
	}

	public static void visit(Node node, int level) {
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node childNode = list.item(i);
			System.out.println(childNode.getNodeName());
			visit(childNode, level + 1);
		}
	}
}
