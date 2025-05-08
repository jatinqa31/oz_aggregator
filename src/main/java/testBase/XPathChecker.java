package testBase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.io.File;

import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XPathChecker {

    public static void main(String[] args) {
    	try {
            // Load HTML from URL
            org.jsoup.nodes.Document jsoupDoc = Jsoup.connect("https://www.zomato.com/partners/onlineordering/orders/")
                                                     .userAgent("Mozilla/5.0")
                                                     .get();

            // Convert to W3C Document
            Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

            // Define XPath
            String xpathToCheck = "//div[contains(text(), 'Ready')]"; // Example XPath

            // Call the utility method
            boolean exists = isXPathPresent(w3cDoc, xpathToCheck);

            System.out.println("XPath exists? " + exists);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    
public static boolean isXPathPresent(Document doc, String xpathExpr) {
    try {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        XPathExpression expression = xpath.compile(xpathExpr);

        Node result = (Node) expression.evaluate(doc, XPathConstants.NODE);
        return result != null;
    } catch (XPathExpressionException e) {
        e.printStackTrace();
        return false;
    }
}

//   	public void xpath_Checker() {
//        try {
//            // Path to your local HTML or XML file
//            File inputFile = new File("path/to/your/file.html");
//
//            // Create DocumentBuilder
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            factory.setNamespaceAware(false); // HTML usually isn't namespace-aware
//            DocumentBuilder builder = factory.newDocumentBuilder();
//
//            // Parse the document
//            Document doc = builder.parse(inputFile);
//
//            // Create XPath
//            XPathFactory xPathFactory = XPathFactory.newInstance();
//            XPath xPath = xPathFactory.newXPath();
//
//            // XPath Expression to check
//            String expression = "//div[@id='myDiv']";  // example XPath
//
//            // Compile and evaluate the XPath
//            XPathExpression xPathExpression = xPath.compile(expression);
//            Node node = (Node) xPathExpression.evaluate(doc, XPathConstants.NODE);
//
//            // Check existence
//            if (node != null) {
//                System.out.println("XPath exists in the document.");
//            } else {
//                System.out.println("XPath does NOT exist in the document.");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//	}

}
