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
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StAXCrawler {
    
    private String baseSitemapURL = "";
    ArrayList<String> sitemapURLs = new ArrayList<>();
    
    private final String inputBrand;
    private final String inputProduct;
    private int crawlDelay = 0;
    private ArrayList<String> disallowedPages = new ArrayList<>();
    private ArrayList<String> foundProducts = new ArrayList<>();
    
    long searchStartTime;
    long searchEndTime;
    long searchDuration;
    
    
    public StAXCrawler(String inputBrand, String inputProduct) {
        this.inputBrand = inputBrand;
        this.inputProduct = inputProduct;
        initializeCrawler();
    }
    
    private void initializeCrawler() {
        URL url;
        InputStream is;
        BufferedReader br;
        String line;
        String[] splitLine;
    
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
        } catch (MalformedURLException ex) {
            Logger.getLogger(StAXCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StAXCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void search() {
        // Start timer
        searchStartTime = System.currentTimeMillis();
        
        try {
            waitCrawlDelay();
            InputStream in = new URL(baseSitemapURL).openStream();
            
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(in);
            
            String tagContent = null;
            
            while(reader.hasNext()) {
                int event = reader.next();
                
                switch(event) {
                    case XMLStreamConstants.START_ELEMENT:
                        // Do nothing
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        tagContent = reader.getText().trim();
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (reader.getLocalName().equals("loc") && tagContent.contains("product") && tagContent.contains("_en-")) {
                            sitemapURLs.add(tagContent);
                        }
                        break;
                }
            } 
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(StAXCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // End timer and calculate duration
        searchEndTime = System.currentTimeMillis();
        searchDuration = searchEndTime - searchStartTime;
        
        findProduct();
    }
    
    public void findProduct() {
        // Start timer
        searchStartTime = System.currentTimeMillis();
        
        String[] splitLine;
        
        try {
            waitCrawlDelay();
            InputStream in = new URL(sitemapURLs.get(0)).openStream();
            
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(in);
            
            String tagContent = null;

            while(reader.hasNext()) {
                int event = reader.next();
                
                switch(event) {
                    case XMLStreamConstants.START_ELEMENT:
                        // Do nothing
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        tagContent = reader.getText().trim();
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (reader.getLocalName().equals("loc") && tagContent.contains(inputProduct)) {
//                            splitLine = tagContent.split("https://www.sephora.com/ca/en/product/");
//                            splitLine = splitLine[1].split("-P");
                            foundProducts.add(tagContent);
//                            foundProducts.add(splitLine[0]);
                        }
                        break;
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(StAXCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(StAXCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        // End timer and calculate duration
        searchEndTime = System.currentTimeMillis();
        searchDuration += searchEndTime - searchStartTime;
    }
    
    private void waitCrawlDelay() {
        try {
            TimeUnit.SECONDS.sleep(crawlDelay);
        } catch (InterruptedException ex) {
            Logger.getLogger(StAXCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getDuration() {
        return Long.toString(TimeUnit.MILLISECONDS.toSeconds(searchDuration)) + " seconds";
    }
    
    public int getNumberFoundProducts() {
        return foundProducts.size();
    }
    
    public String getFoundProducts() {
        return foundProducts.get(0);
    }
    
//    public String getFoundProducts() {
//        return foundProducts.toString();
//    }

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
