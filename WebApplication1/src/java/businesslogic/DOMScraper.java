package businesslogic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.jsoup.safety.Cleaner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class DOMScraper {
    
    private final String productURL;
    private int crawlDelay = 0;
    private String results = "";
      
    public DOMScraper(String productURL, int crawlDelay) {
        this.productURL = "https://www.sephora.com/ca/en/product/the-porefessional-face-primer-P264900";
//    this.productURL = productURL;    
        this.crawlDelay = crawlDelay;
        scrapePage();
    }
    
    private void scrapePage()  {
        InputStream in;
        String inputStreamString;
        
        DocumentBuilderFactory documentBuilderFactory;
        DocumentBuilder documentBuilder;
        Document document;
        XPath xPath;
        
        NodeList nodeListProductTop;
        NodeList nodeListProductBottom;  

//        try {
            waitCrawlDelay();
            
            
            // Using Selenium WebDriver, get the entire page after JavaScript runs
            System.setProperty("webdriver.chrome.driver","C:\\webdrivers\\chromedriver.exe");
            WebDriver driver = new ChromeDriver();
            driver.get("https://www.sephora.com/ca/en/product/the-porefessional-face-primer-P264900");
            waitCrawlDelay();
            
            // Clean HTML page with JSoup
            org.jsoup.nodes.Document jSoupDirtyDoc = Jsoup.parse(driver.getPageSource());
//            org.jsoup.nodes.Document jSoupCleanDoc = new Cleaner(Whitelist.relaxed()
//                .addTags("svg", "image", "h1", "h2", "main", "nav")
//                .addAttributes(":all", "class", "data-comp", "data-sephid")
//                .removeTags("img"))
//                .clean(jSoupDirtyDoc);
            
            System.out.println(jSoupDirtyDoc.html());

//            documentBuilderFactory = DocumentBuilderFactory.newInstance();
//            documentBuilder = documentBuilderFactory.newDocumentBuilder();
//            document = documentBuilder.parse(new InputSource(new StringReader(jSoupCleanDoc.html())));
//            URL url = new URL(productURL);
//            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//            String productPageHTML = "";
//            String inputLine;
//            while((inputLine = br.readLine()) != null)
//                productPageHTML += inputLine;
//            br.close();
//            
//            boolean valid = Jsoup.isValid(productPageHTML, Whitelist.basic());
//            if (!valid) {
//                
//            }
//            
//            in = new URL(productURL).openStream();            
//            documentBuilderFactory = DocumentBuilderFactory.newInstance();
//            documentBuilder = documentBuilderFactory.newDocumentBuilder();
//            document = (Document) documentBuilder.parse(in);

//            xPath = XPathFactory.newInstance().newXPath();
//            
//            // Create XPathExpressions
//            XPathExpression brandExp = xPath.compile("./div[@data-comp='DisplayName Flex Box']");
//            XPathExpression productNameExp = xPath.compile("./div[@data-comp='DisplayName Flex Box']");
//            XPathExpression sizeExp = xPath.compile("./div[@data-comp='SizeAndItemNumber Box']");
//            XPathExpression priceExp = xPath.compile("./div[@data-comp='Price Box']");
//            
//            String priceRegex = "C$\\d+.\\d{2}";
//            
//            // Grab only relevant divs that contain useful product data
//            nodeListProductTop = (NodeList)xPath.compile("//div[@data-comp='RegularProductTop']").evaluate(document, XPathConstants.NODESET);
//            nodeListProductBottom = (NodeList)xPath.compile("//div[@data-comp='RegularProductBottom']").evaluate(document,XPathConstants.NODESET);
//        
//            
//            for(int i = 0; i < nodeListProductTop.getLength(); i++) {
//                if (priceExp.evaluate(nodeListProductTop.item(i)).matches(priceRegex)) {
//                    results = priceExp.evaluate(nodeListProductTop.item(i));
//                }
//            }
       
//        } catch (IOException ex) {
//            Logger.getLogger(DOMScraper.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SAXException ex) {
//            Logger.getLogger(DOMScraper.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParserConfigurationException ex) {
//            Logger.getLogger(DOMScraper.class.getName()).log(Level.SEVERE, null, ex);
//        }
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
