package businesslogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Crawler {
    
    private String baseSitemapURL = "";
    private final String inputBrand;
    private final String inputProduct;
    private int crawlDelay = 0;
    private ArrayList<String> disallowedPages = new ArrayList<>();
    private URL url;
    private InputStream is = null;
    private BufferedReader br;
    private String line;
    private String[] splitLine;
    private ArrayList<String> allProducts = new ArrayList<>();
    
    public Crawler(String inputBrand, String inputProduct) {
        this.inputBrand = inputBrand;
        this.inputProduct = inputProduct;
        initializeCrawler();
    }
    
    private void initializeCrawler() {
        try {
            url = new URL("https://www.sephora.com/robots.txt");
            is = url.openStream();
            br = new BufferedReader(new InputStreamReader(is));
            
            while ((line = br.readLine()) != null) {
                if (line.contains("Disallow")) {
                    splitLine = line.split(": ");
                    disallowedPages.add(splitLine[1]);
                } else if (line.contains("Crawl-delay")) {
                    splitLine = line.split(": ");
                    crawlDelay = Integer.parseInt(splitLine[1]);
                } else if (line.contains("Sitemap")) {
                    splitLine = line.split(": ");
                    baseSitemapURL = splitLine[1];
                }
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    public String searchForProductPage() {
        ArrayList<String> sitemapURLs = new ArrayList<>();
        
        try { // Get URLs from sitemap using XPath
            TimeUnit.SECONDS.sleep(crawlDelay);
            InputStream in = new URL(baseSitemapURL).openStream();
            
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = (Document) documentBuilder.parse(in);
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList)xPath.compile("//sitemap").evaluate(document, XPathConstants.NODESET);
            XPathExpression xPathExp = xPath.compile("./loc");
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                if(xPathExp.evaluate(nodeList.item(i)).contains("product") && xPathExp.evaluate(nodeList.item(i)).contains("_en-")) {
                    sitemapURLs.add(xPathExp.evaluate(nodeList.item(i)));
                }
            }
            
            for(int i = 0; i < sitemapURLs.size(); i++) {
                TimeUnit.SECONDS.sleep(crawlDelay);
                in = new URL(sitemapURLs.get(i)).openStream();
                documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilder = documentBuilderFactory.newDocumentBuilder();
                document = (Document) documentBuilder.parse(in);
                xPath = XPathFactory.newInstance().newXPath();
                nodeList = (NodeList)xPath.compile("//url").evaluate(document, XPathConstants.NODESET);
                xPathExp = xPath.compile("./loc");
                
                for(int j = 0; j < nodeList.getLength(); j++) {
                    if(xPathExp.evaluate(nodeList.item(j)).contains(inputProduct)) {
                        splitLine = xPathExp.evaluate(nodeList.item(j)).split("https://www.sephora.com/ca/en/product/");
                        splitLine = splitLine[1].split("-P");
                        allProducts.add(splitLine[0]);
                    }
                }
            }
            
            return xPathExp.toString();
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "No urls grabbed";
    }
    
    public String getAllProducts() {
        return allProducts.toString();
    }

    public String getDisallowedPages() {
        return disallowedPages.toString();
    }
    
    public String getSitemapURL() {
        return baseSitemapURL;
    }
    
    public int getCrawlDelay() {
        return crawlDelay;
    }
    
    public String getInputBrand() {
        return inputBrand;
    }
    
    public String getInputProduct() {
        return inputProduct;
    }

}
