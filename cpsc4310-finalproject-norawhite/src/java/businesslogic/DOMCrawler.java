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

/**
 * DOMCrawler looks for URLs to navigate to to further the search for product
 * data. Handles the processing of any sitemaps and the robots.txt file.
 * 
 * @author Nora White
 */
public class DOMCrawler {
    
    private String baseSitemapURL = "";
    ArrayList<String> sitemapURLs = new ArrayList<>();
    
    private final String inputBrand,
            inputProduct,
            inputURL;
    
    long searchStartTime,
            searchEndTime,
            searchDuration;
    
    int disallowedPagesSize;
    
    private int crawlDelay = 0;
    private ArrayList<String> disallowedPages = new ArrayList<>();
    private ArrayList<String> foundProducts = new ArrayList<>();
    
    /**
     * Constructor for the DOMCrawler class. Accepts 3 parameters, the brand,
     * product name, and the url to search for products on. Once the parameters
     * have been saved, it initializes itself by accessing the robots.txt file.
     * 
     * @param inputBrand    the brand input by the user to search for
     * @param inputProduct  the product name input by the user to search for
     * @param inputURL      the url input by the user to search for products on
     */
    public DOMCrawler(String inputBrand, String inputProduct, String inputURL) {
        this.inputBrand = inputBrand;
        this.inputProduct = inputProduct;
        this.inputURL = inputURL;
        initializeCrawler();
    }
    
    /**
     * This function navigates to the robots.txt file of the input site. It
     * opens a buffered reader to read the input stream of the txt file. Line by 
     * line, it identifies the disallowed pages, the crawl delay, and the starting
     * sitemap.
     */
    private void initializeCrawler() {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        String[] splitLine;
    
        try {
            url = new URL(inputURL + "robots.txt");
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
            disallowedPagesSize = disallowedPages.size();
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
    
    /**
     * Opens an input stream for the sitemap given by robots.txt. Using 
     * DocumentBuilderFactory and XPath, the crawler sets the NodeList to the
     * 'sitemap' tag, and then extracts all 'loc's (url locations) of other
     * sitemaps.The reader is able to find the correct sitemap that contains 
     * product info. Values have been hardcoded specifically for Sephora's URL 
     * style.
     */
    public void search() {
        // Start timer
        searchStartTime = System.currentTimeMillis();
        
        try {
            waitCrawlDelay();
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
        } catch (MalformedURLException ex) { 
            Logger.getLogger(DOMCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SAXException | ParserConfigurationException | XPathExpressionException ex) {
            Logger.getLogger(DOMCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // End timer and calculate duration
        searchEndTime = System.currentTimeMillis();
        searchDuration = searchEndTime - searchStartTime;
        
        findProduct();
    }
    
    /**
     * Using the same method as the search() function, DOM processes the given
     * sitemap that contains the URLs of all products. It looks for the URL for 
     * any products that match the search query. Based on the structure of the 
     * URL, the URL cannot be ignored based on the input brand. The brand will 
     * need to be checked on the HTML page to determine if the product 
     * completely matches the search query.
     */
    public void findProduct() {
        
        // Start timer
        searchStartTime = System.currentTimeMillis();
        
        String[] splitLine;   
        
        InputStream in;
        DocumentBuilderFactory documentBuilderFactory;
        DocumentBuilder documentBuilder;
        Document document;
        XPath xPath;
        NodeList nodeList;
        XPathExpression xPathExp;
        
        try {
            for(int i = 0; i < sitemapURLs.size(); i++) {
                waitCrawlDelay();
                
                in = new URL(sitemapURLs.get(i)).openStream();
                documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilder = documentBuilderFactory.newDocumentBuilder();
                document = (Document) documentBuilder.parse(in);
                xPath = XPathFactory.newInstance().newXPath();
                nodeList = (NodeList) xPath.compile("//url").evaluate(document, XPathConstants.NODESET);
                xPathExp = xPath.compile("./loc");
                
                String lineContent;
                
                for(int j = 0; j < nodeList.getLength(); j++) {
                    lineContent = xPathExp.evaluate(nodeList.item(j));
                    if(lineContent.contains(inputProduct)) {
                        for (int k = 0; k < disallowedPagesSize; k++) {
                            if (lineContent.contains(disallowedPages.get(k))) {
                                break;
                            } else if (disallowedPagesSize-1 == k) {
                                foundProducts.add(lineContent);
                            }
                        }
//                        splitLine = xPathExp.evaluate(nodeList.item(j)).split("https://www.sephora.com/ca/en/product/");
//                        splitLine = splitLine[1].split("-P");
//                        foundProducts.add(splitLine[0]);
                    }
                }
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(DOMCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException ex) {
            Logger.getLogger(DOMCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // End timer and calculate duration
        searchEndTime = System.currentTimeMillis();
        searchDuration += searchEndTime - searchStartTime;
    }
    
    /**
     * Waits the amount of time specified in the robots.txt file to avoid 
     * having IP blocked by site owner.
     */
    private void waitCrawlDelay() {
        try {
            TimeUnit.SECONDS.sleep(crawlDelay);
        } catch (InterruptedException ex) {
            Logger.getLogger(DOMCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Calculates the duration of time the crawler ran for and 
     * appends "seconds" to it.
     * 
     * @return  a string representing the amount of time the crawler ran for
     */
    public long getDuration() {
        return TimeUnit.MILLISECONDS.toSeconds(searchDuration);
    }
    
    /**
     * Returns the integer value of how many product urls were found that match
     * the inputProduct.
     * 
     * @return  an integer representing the number of urls found that match 
     *          the inputProduct.
     */
    public int getNumberFoundProducts() {
        return foundProducts.size();
    }
    
    /**
     * Returns the ArrayList of all product urls that were found that 
     * match the inputProduct.
     * 
     * @return  an ArrayList of the String value of each product url that 
     *          a scraper would need to visit.
     */
    public ArrayList<String> getFoundProducts() {
        return foundProducts;
    }

    /**
     * Returns the disallowed pages as a string value.
     * 
     * @return  a String representation of the disallowedPages ArrayList
     */
    public String getDisallowedPages() {
        return disallowedPages.toString();
    }
    
    /**
     * Returns the initial sitemap that robots.txt contains
     * 
     * @return  baseSitemapURL  the sitemap given by robots.txt
     */
    public String getSitemapURL() {
        return baseSitemapURL;
    }
    
    /**
     * Returns the crawl delay specified in the robots.txt
     * 
     * @return  crawlDelay  the amount of delay specified in the robots.txt file
     *                      that the crawler/scraper is required to wait between
     *                      each request
     */
    public int getCrawlDelay() {
        return crawlDelay;
    }
    
    /**
     * Returns the brand input by the user.
     * 
     * @return  inputBrand  the brand value given by the user in the input form.
     */
    public String getInputBrand() {
        return inputBrand;
    }
    
    /**
     * Returns the product name input by the user.
     * 
     * @return  inputProduct    the product name value given by the user in the 
     *                          input form.
     */
    public String getInputProduct() {
        return inputProduct;
    }
}
