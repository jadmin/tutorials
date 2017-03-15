/*
 * @(#)XmlUtil.java	2011-9-8
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.xml;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XmlUtil
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: XmlUtil.java 2011-9-8 下午02:39:49 Exp $
 */
public class XmlUtil {

	/**
     * 在内存中创建一个Document文档对象
     * @return doc
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public static org.w3c.dom.Document newDocument() throws 
            ParserConfigurationException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }

    /**
     * 通过xml文件路径获取一个Document文档对象
     * @param xmlPath XML文件的路径
     * @return doc 该XML文件的Document对象
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public static org.w3c.dom.Document getDocument(String xmlPath) throws 
            SAXException, IOException, ParserConfigurationException {
        return getDocument(new File(xmlPath));
    }
    
    /**
     * 读取file对象并分析，返回一个Document对象
     * @param file 需要分析的文件
     * @return doc Document对象
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public static org.w3c.dom.Document getDocument(File file) throws 
            SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }
    
    /**
     * 该方法用于将doc文件保存至xmlPath, 将内存中的整个Document保存至路径
     * @param doc 已经读入内存的document文档对象
     * @param xmlPath 保存路径
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws javax.xml.transform.TransformerException
     */
    public static void saveDocument(Document doc, String xmlPath) throws 
            TransformerConfigurationException, TransformerException {
        saveDocument(doc, new File(xmlPath));
    }
    
    /**
     * 该方法用于将doc文件保存至file, 将内存中的整个Document保存至file所在的路径
     * @param doc 已经读入内存的document文档对象
     * @param file 保存的文件对象
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws javax.xml.transform.TransformerException
     */
    public static void saveDocument(Document doc, File file) throws 
            TransformerConfigurationException, TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();    
        Transformer transformer = tFactory.newTransformer();  
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
    
    /**
     * 输出doc
     * @param doc
     * @param writer
     * @throws javax.xml.transform.TransformerConfigurationException
     * @throws javax.xml.transform.TransformerException
     */
    public static void write(Document doc, Writer writer) throws 
            TransformerConfigurationException, TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();    
        Transformer transformer = tFactory.newTransformer(); 
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
    }
}
