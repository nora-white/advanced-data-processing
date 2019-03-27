package businesslogic;

//import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StAXScraper {
    
    private final String productURL;
    private int crawlDelay = 0;
    private String results = "";
      
    public StAXScraper(String productURL, int crawlDelay) {
        this.productURL = productURL;
        this.crawlDelay = crawlDelay;
        scrapePage();
    }
    
    private void scrapePage() {        
        try {
            waitCrawlDelay();
            InputStream in = new URL(productURL).openStream();
            
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(in);
            
            String tagContent = null;
//            String localName = null;
            
            while(reader.hasNext()) {
                int event = reader.next();
//                localName = reader.getLocalName();
                
                switch(event) {
                    case XMLStreamConstants.START_ELEMENT:
                        
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        results += " " + reader.getText().trim();
                        break;
                    case XMLStreamConstants.END_ELEMENT:
//                        results += " " + tagContent;
                        break;
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(StAXScraper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | XMLStreamException ex) {
            Logger.getLogger(StAXScraper.class.getName()).log(Level.SEVERE, null, ex);
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
