package businesslogic;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMScraper {
    
    private final String productURL;
    private int crawlDelay = 0;
    private String results = "";
      
    public DOMScraper(String productURL, int crawlDelay) {
        this.productURL = productURL;
        this.crawlDelay = crawlDelay;
        scrapePage();
    }
    
    private void scrapePage() {
        InputStream in;
        DocumentBuilderFactory documentBuilderFactory;
        DocumentBuilder documentBuilder;
        Document document;
        XPath xPath;
        
        NodeList nodeListProductTop;
        NodeList nodeListProductBottom;
        
        
        
        try {
            waitCrawlDelay();
            in = new URL(productURL).openStream();
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = (Document) documentBuilder.parse(in);
            xPath = XPathFactory.newInstance().newXPath();
            
            // Grab only relevant divs that contain useful product data
            nodeListProductTop = (NodeList)xPath.compile("//div[@data-comp='RegularProductTop']").evaluate(document, XPathConstants.NODESET);
            nodeListProductBottom = (NodeList)xPath.compile("//div[@data-comp='RegularProductBottom']").evaluate(document,XPathConstants.NODESET);
        
        
        } catch (MalformedURLException ex) { 
            Logger.getLogger(DOMCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SAXException | ParserConfigurationException ex) {
            Logger.getLogger(DOMCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(DOMScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    private void waitCrawlDelay() {
        try {
            TimeUnit.SECONDS.sleep(crawlDelay);
        } catch (InterruptedException ex) {
            Logger.getLogger(StAXCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String getResults() {
        return results;
    }
}
