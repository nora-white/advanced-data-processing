package businesslogic;

import domain.Product;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DOMScraper {
    
    private final String productURL, inputBrand;
    private final int crawlDelay;
    private Product product = new Product();
      
    public DOMScraper(String productURL, String inputBrand, int crawlDelay) {
        this.productURL = productURL;
        this.inputBrand = inputBrand.toLowerCase();
        this.crawlDelay = crawlDelay;
        scrapePage();
    }
    
    private void scrapePage()  {        
        DocumentBuilderFactory documentBuilderFactory;
        DocumentBuilder documentBuilder;
        Document document;
        XPath xPath;
        NodeList nodeListProductTop;

        try {
            waitCrawlDelay();
            
            // Using Selenium WebDriver, get the entire page after JavaScript runs
            System.setProperty("webdriver.chrome.driver","C:\\webdrivers\\chromedriver.exe");
            WebDriver driver = new ChromeDriver();
            driver.get(productURL);
            waitCrawlDelay();
            
            // Clean HTML page with JSoup
            org.jsoup.nodes.Document jSoupDirtyDoc = Jsoup.parse(driver.getPageSource());
            driver.quit();
            org.jsoup.nodes.Document jSoupCleanDoc = new Cleaner(Whitelist.relaxed()
                .addTags("svg", "image", "h1", "h2", "main", "nav")
                .addAttributes(":all", "class", "data-comp", "data-sephid")
                .removeTags("img", "script", "style", "iframe", "br"))
                .clean(jSoupDirtyDoc);

            // Build the document for xPath to work off of
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(new InputSource(new StringReader(jSoupCleanDoc.html())));
            
            // Create XPathExpressions
            xPath = XPathFactory.newInstance().newXPath();
            XPathExpression brandExp = xPath.compile("//h1[@data-comp='DisplayName Flex Box']/a/span");
            XPathExpression productNameExp = xPath.compile("//h1[@data-comp='DisplayName Flex Box']/span");
            XPathExpression sizeExp = xPath.compile("//span[contains(text(),'Size')] | //span[contains(text(), 'oz')] | //span[contains(text(), 'mL')]");
            XPathExpression priceExp = xPath.compile("//div[@data-comp='Price Box']");
//            XPathExpression imageExp = xPath.compile("//*[name()='image']/@xlink:href");
            
            // Grab only relevant divs that contain useful product data
            nodeListProductTop = (NodeList) xPath.compile("//div[@data-comp='RegularProductTop']").evaluate(document, XPathConstants.NODESET);
           
            if ((brandExp.evaluate(nodeListProductTop.item(0)).toLowerCase()).contains(inputBrand)) {
                product.setBrand(brandExp.evaluate(nodeListProductTop.item(0)));
                product.setProducturl(productURL);
                product.setPrice(priceExp.evaluate(nodeListProductTop.item(0)));
                product.setName(productNameExp.evaluate(nodeListProductTop.item(0)));

                try { // Check for edge cases with differing ways of displaying size
                    String[] splitSize;
                    if (sizeExp.evaluate(nodeListProductTop.item(0)).contains("- ")) {
                        splitSize = sizeExp.evaluate(nodeListProductTop.item(0)).split("- ");
                        product.setSize(splitSize[1]);
                    } else if (sizeExp.evaluate(nodeListProductTop.item(0)).contains(": ")) {
                        splitSize = sizeExp.evaluate(nodeListProductTop.item(0)).split(": ");
                        product.setSize(splitSize[1]);
                    } else if (sizeExp.evaluate(nodeListProductTop.item(0)).contains("E ")) {
                        splitSize = sizeExp.evaluate(nodeListProductTop.item(0)).split("E ");
                        product.setSize(splitSize[1]);
                    } else {
                        product.setSize(sizeExp.evaluate(nodeListProductTop.item(0)));
                    }
                } catch (Exception e) {
                    product.setSize("");
                }
            
                product.setImgurl("");
            } else {
                product = null;
            }
            
       
        } catch (IOException ex) {
            Logger.getLogger(DOMScraper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(DOMScraper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DOMScraper.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public Product getProduct() {
        return product;
    }
}
