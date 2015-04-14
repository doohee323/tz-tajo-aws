package utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xpath.XPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <pre>
 * </pre>
 * 
 * @version 1.0
 */
public class XmlUtil {

  /**
   */
  private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);

  /**
   * <pre>
   * </pre>
   * 
   * @param input
   * @param encoding
   * @return
   */
  public static Map<String, ?> XmlToMulti(Map<String, Object> input, String encoding) {
    String aXmlStr = input.get("xmlStr").toString();
    String aSchemaPath = input.get("schemaPath").toString();
    String aDataPath = input.get("dataPath").toString();
    Document doc = XmlUtil.parsing(aXmlStr, encoding, false);
    doc.getDocumentElement().normalize();
    Map<String, Object> inputData = new HashMap<String, Object>();
    inputData.put("result", aXmlStr);
    inputData.put("xPath", aSchemaPath);
    List<Map<String, Object>> column = XmlToMultiByAttri(inputData);
    inputData.put("xPath", aDataPath);
    List<Map<String, Object>> data = XmlToMultiByAttri(inputData);

    Map<String, Object> lHolder = new HashMap<String, Object>();
    Map<String, Object> header = new HashMap<String, Object>();
    if (data.get(0).containsKey("qRowCnt")) {
      header.put("qRowCnt", data.get(0).get("qRowCnt"));
    }
    if (data.get(0).containsKey("qType")) {
      header.put("qType", data.get(0).get("qType"));
    }
    if (data.get(0).containsKey("qMessage")) {
      header.put("qMessage", data.get(0).get("qMessage"));
    }

    lHolder.put("HEADER", header);
    lHolder.put("COLUMN", column);
    lHolder.put("BODY", data);
    return lHolder;
  }

  /**
   * <pre>
   * </pre>
   * 
   */
  public static Document parsing(String xmlStr, String encoding, boolean validating) {
    Document temp = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(validating);
    factory.setCoalescing(true); // Convert CDATA to Text nodes
    factory.setIgnoringComments(true);
    factory.setIgnoringElementContentWhitespace(true);
    DocumentBuilder builder = null;
    try {
      builder = factory.newDocumentBuilder();
      byte[] byteArray = null;
      if (encoding != null && !encoding.equals("")) {
        byteArray = xmlStr.getBytes(encoding);
      } else {
        byteArray = xmlStr.getBytes();
      }
      builder.parse(new InputSource(new StringReader(xmlStr.toString())));

      ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
      temp = builder.parse(bis);
    } catch (ParserConfigurationException e) {
      System.out.println(e.toString());
    } catch (SAXException e) {
      System.out.println(e.toString());
    } catch (IOException e) {
      System.out.println(e.toString());
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return temp;
  }

  /**
   * <pre>
   * </pre>
   */
  public static List<Map<String, Object>> XmlToMultiByAttri(Map<String, Object> inputData) {
    return XmlToMultiByAttri(inputData, "utf-8");
  }

  /**
   * <pre>
   * </pre>
   */
  public static List<Map<String, Object>> XmlToMultiByAttri(Map<String, Object> inputData,
      String encoding) {
    String strResult = inputData.get("result").toString();
    String xPath = inputData.get("xPath").toString();
    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    try {
      Document doc = XmlUtil.parsing(strResult, encoding, false);
      doc.getDocumentElement().normalize();
      result = XmlUtil.getNodeListByAttri(doc, xPath);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return result;
  }

  /**
   * <pre>
   * </pre>
   */
  // <feature>
  // <code name="APP1126">
  // <code name="APP1126">
  // <code name="APP1126">
  // <code name="APP1126">
  // </feature>
  public static List<Map<String, Object>> getNodeListByAttri(Document doc, String path) {
    List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
    try {
      NodeList nodeList = XPathAPI.selectNodeList(doc, path);
      if (nodeList != null) {
        int nSize = nodeList.getLength();
        for (int i = 0; i < nSize; i++) {
          Map<String, Object> input = new HashMap<String, Object>();
          NamedNodeMap nnm = nodeList.item(i).getAttributes();
          int nSize2 = nnm.getLength();
          for (int j = 0; j < nSize2; j++) {
            Node node = nnm.item(j);
            if (node != null) {
              input.put(node.getNodeName(), node.getNodeValue());
            }
          }
          map.add(map.size(), input);
        }
      }
    } catch (javax.xml.transform.TransformerException tr) {
    }
    return map;
  }
}
