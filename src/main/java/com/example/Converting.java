/*
 * README - Converting.java
 *
 * Description:
 *
 * The Converting.java file is a utility class designed to parse an XML file representing a Simulink model and convert it
 * into a set of Block and Line objects. The primary function of this class is convertXMLToObjects, which takes an XML
 * file path as input and returns an array of Block and Line objects. These objects can then be used for further
 * processing, analysis, or visualization of the Simulink model.
 *
 * Functions:
 *
 * 1. splitTextToIntArr(String arr): A utility function to split a string containing integers separated by commas or
 * semicolons into an array of integers.
 *
 * 2. splitTextToStrArr(String arr): A utility function to split a string containing substrings separated by '#' or ':'
 * into an array of strings.
 *
 * 3. convertXMLToObjects(String path): The main function of this class, responsible for parsing the XML file and
 * converting its content into Block and Line objects using DOM (Document Object Model) and XPath. The function can be
 * broken down into the following steps:
 * 
 *    a. XML parsing and Block extraction: Parses the XML file using DOM and XPath, and extracts the "Block" elements,
 *    creating a `Block` object for each one and adding it to an array.
 *    
 *    b. XML parsing and Line extraction: Parses the XML file using DOM and XPath, and extracts the "Line" elements,
 *    creating a `Line` object for each one and adding it to an array. This process includes extracting the line's
 *    source and destination information, as well as its points (corners) and branches.
 *
 * Usage:
 *
 * To use this class, simply call the convertXMLToObjects function with the path to the XML file representing the
 * Simulink model. The function will return an array containing the Block and Line objects representing the model's
 * components.
 *
 * Example:
 *
 * Object[] result = Converting.convertXMLToObjects("path/to/your/xml/file.xml");
 * Block[] blockArr = (Block[]) result[0];
 * Line[] lineArr = (Line[]) result[1];
 *
 */

package com.example;
//Converting.java

// Import necessary Java libraries
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.geometry.Point2D;

public class Converting {
    static int[] splitTextToIntArr(String arr) {
        String temp = arr.replaceAll("\\[|\\]", "");
        if (temp.isBlank()) {
            return new int[0];
        }
        String[] parts = temp.split("[,;]");

        int[] array = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {

            array[i] = Integer.parseInt(parts[i].trim());

        }
        return array;
    }

    static String[] splitTextToStrArr(String arr) {
        if (arr.isBlank()) {
            return new String[0];
        }
        String[] parts = arr.split("[#:]");
        return parts;

    }

    static Object[] convertXMLToObjects(String path)
            throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Create a DocumentBuilder object to parse the XML file
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Parse the XML file and create a Document object
        Document doc = builder.parse(new File(path));

        // Get the root element of the XML file
        Element root = doc.getDocumentElement();

        // Get all "Block" elements and their child nodes
        NodeList blockNodes = root.getElementsByTagName("Block");
        Block[] blockArr = new Block[blockNodes.getLength()];

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();

        // Temp variables for loop

        for (int i = 0; i < blockArr.length; i++) {
            Element blockElement = (Element) blockNodes.item(i);

            // Extract the relevant data from the "Block" element
            String blockName = blockElement.getAttribute("Name");
            int blockSid = Integer.parseInt(blockElement.getAttribute("SID"));

            String portsStr = (String) xpath.evaluate("/System/Block[" + (i + 1) + "]/P[@Name='Ports']", root,
                    XPathConstants.STRING);
            int[] portsArr = splitTextToIntArr(portsStr.trim());

            String posStr = (String) xpath.evaluate("/System/Block[" + (i + 1) + "]/P[@Name='Position']", root,
                    XPathConstants.STRING);
            int[] posArr = splitTextToIntArr(posStr.trim());

            int inputN, outputN;
            if (portsArr.length >= 2) {
                inputN = portsArr[0];
                outputN = portsArr[1];
            } else if (portsArr.length == 1) {
                inputN = portsArr[0];
                outputN = 1;
            } else {
                inputN = 1;
                outputN = 1;
            }
            String isMirroredStr = ((String) xpath.evaluate("/System/Block[" + (i + 1) + "]/P[@Name='BlockMirror']", root,
                    XPathConstants.STRING));
            boolean isMirroed= isMirroredStr!=null?isMirroredStr.trim().compareTo("on")==0:false;
            blockArr[i] = new Block(blockName, blockSid, new Point2D(posArr[0], posArr[1]),
                    new Point2D(posArr[2], posArr[3]), inputN,
                    outputN,isMirroed);
            ;
        }

        // Get all "Line" elements and their child nodes
        NodeList lineNodes = root.getElementsByTagName("Line");
        // Temp variables for loop
        Line[] lineArr = new Line[lineNodes.getLength()];

        for (int i = 0; i < lineArr.length; i++) {
            Element lineElement = (Element) lineNodes.item(i);
            List<Point2D> cornerList = new ArrayList<Point2D>();
            List<Branch> branchList = new ArrayList<Branch>();

            String srcStr = (String) xpath.evaluate("/System/Line[" + (i + 1) + "]/P[@Name='Src']", root,
                    XPathConstants.STRING);
            String[] srcArr = splitTextToStrArr(srcStr.trim());

            int srcId = Integer.parseInt(srcArr[0]);

            boolean isSrcInput = srcArr[1].trim().compareTo("in") == 0;
            int srcPortNumber = Integer.parseInt(srcArr[2]);

            String dstStr = (String) xpath.evaluate("/System/Line[" + (i + 1) + "]/P[@Name='Dst']", root,
                    XPathConstants.STRING);
            String[] dstArr = splitTextToStrArr(dstStr.trim());
            int dstId, dstPortNumber;
            boolean isDstInput;
            if (dstArr.length == 0) {
                dstId = -1;
                isDstInput = false;
                dstPortNumber = -1;
            } else {
                dstId = Integer.parseInt(dstArr[0]);
                isDstInput = dstArr[1].trim().compareTo("in") == 0;
                dstPortNumber = Integer.parseInt(dstArr[2]);
            }
            String cornersStr = (String) xpath.evaluate("/System/Line[" + (i + 1) + "]/P[@Name='Points']", root,
                    XPathConstants.STRING);
            if (!cornersStr.isEmpty()) {
                int[] cornerArr = splitTextToIntArr(cornersStr);
                for (int j = 0; j < cornerArr.length; j += 2) {
                    cornerList.add(new Point2D(cornerArr[j], cornerArr[j + 1]));
                }
            }
            // Checing if there is branches
            if (dstId == -1) {
                NodeList branchNodes = lineElement.getElementsByTagName("Branch");

                for (int j = 0; j < branchNodes.getLength(); j++) {
                    List<Point2D> branchCornerList = new ArrayList<Point2D>();

                    dstStr = (String) xpath.evaluate(
                            "/System/Line[" + (i + 1) + "]/Branch[" + (j + 1) + "]/P[@Name='Dst']",
                            root,
                            XPathConstants.STRING);
                    dstArr = splitTextToStrArr(dstStr.trim());

                    int branchDstId = Integer.parseInt(dstArr[0]);
                    boolean isBranchDstInput = dstArr[1].trim().compareTo("in") == 0;
                    int branchDstPortNumber = Integer.parseInt(dstArr[2]);

                    cornersStr = (String) xpath.evaluate(
                            "/System/Line[" + (i + 1) + "]/Branch[" + (j + 1) + "]/P[@Name='Points']",
                            root,
                            XPathConstants.STRING);
                    if (!cornersStr.isEmpty()) {
                        int[] cornerArr = splitTextToIntArr(cornersStr);
                        for (int k = 0; k < cornerArr.length; k += 2) {
                            branchCornerList.add(new Point2D(cornerArr[k], cornerArr[k + 1]));
                        }
                    }
                    branchList.add(new Branch(branchDstId, isBranchDstInput, branchDstPortNumber, branchCornerList));
                }
            }
            lineArr[i] = new Line(srcId, dstId, isSrcInput, isDstInput, srcPortNumber, dstPortNumber,
                    cornerList, branchList);
        }

        // Return the objects as an array
        return new Object[] { blockArr, lineArr };
    }
}
