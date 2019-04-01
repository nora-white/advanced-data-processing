package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import businesslogic.DOMCrawler;
import businesslogic.StAXCrawler;
import businesslogic.JSONBuilder;
import businesslogic.DOMScraper;
import domain.Product;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


@WebServlet(name = "IndexServlet", urlPatterns = {"/IndexServlet"})
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);  
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Initialize input parameters
        String inputBrand = request.getParameter("inputBrand");
        String inputProduct = request.getParameter("inputProduct");
        String inputURL = request.getParameter("inputURL");
        String runDOMCrawler = request.getParameter("runDOMCrawler");
        
        request.setAttribute("inputBrand", inputBrand);
        request.setAttribute("inputProduct", inputProduct);
        request.setAttribute("inputURL", inputURL);
        ArrayList<Product> scrapedProducts = new ArrayList<>();
        
        // Read in saved products from JSON
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.getJsonFromFile();
        ArrayList<Product> jsonProducts = jsonBuilder.getJsonProductList();
        request.setAttribute("products", jsonProducts);
        
        // Run the crawlers
        if (runDOMCrawler == null) {
            request.setAttribute("DOMCrawlerNotRun", true);
        } else {
            runDOMCrawler(request, inputBrand, inputProduct, inputURL);
        }
        
        StAXCrawler staxCrawler = runStAXCrawler(request, inputBrand, inputProduct, inputURL);
        ArrayList<String> foundProductURLsStAX = staxCrawler.getFoundProducts();
        
        // Find product info and time how long it takes
        long scraperStartTime = System.currentTimeMillis();
        for (int i = 0; i < foundProductURLsStAX.size(); i++) {
            DOMScraper domScraper = new DOMScraper(foundProductURLsStAX.get(i), inputBrand.toLowerCase(), staxCrawler.getCrawlDelay());
            if (domScraper.getProduct() != null)
                scrapedProducts.add(domScraper.getProduct());
        }
        long scraperEndTime = System.currentTimeMillis();
        long domScraperDuration = TimeUnit.MILLISECONDS.toSeconds(scraperEndTime - scraperStartTime);
        request.setAttribute("domScraperDuration", Long.toString(domScraperDuration));
        request.setAttribute("domScraperAverageTime", (domScraperDuration/scrapedProducts.size()));
                
        // Set products
        request.setAttribute("scrapedProducts", scrapedProducts);
        
        // Set true for the search being complete
        request.setAttribute("searchCompleted", true);

        getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);  

    }
    
    private void runDOMCrawler(HttpServletRequest request, String inputBrand, String inputProduct, String inputURL) {
        
        // Create DOMCrawler
        DOMCrawler domCrawler = new DOMCrawler(inputBrand.toLowerCase(), inputProduct.toLowerCase(), inputURL);
        
        // Begin search
        domCrawler.search();
        request.setAttribute("domTotalResults", domCrawler.getNumberFoundProducts());
        request.setAttribute("domTime", Long.toString(domCrawler.getDuration()));
        request.setAttribute("domAverageTime", (domCrawler.getDuration()/domCrawler.getNumberFoundProducts()));
    }
    
    private StAXCrawler runStAXCrawler(HttpServletRequest request, String inputBrand, String inputProduct, String inputURL) {
        
        // Create StAXCrawler
        StAXCrawler staxCrawler = new StAXCrawler(inputBrand.toLowerCase(), inputProduct.toLowerCase(), inputURL);
        
        // Set robots.txt info
        request.setAttribute("sitemapURL", staxCrawler.getBaseSitemapURL());
        request.setAttribute("crawlDelay", staxCrawler.getCrawlDelay());
        
        // Begin search
        staxCrawler.search();
        request.setAttribute("staxTotalResults", staxCrawler.getNumberFoundProducts());
        request.setAttribute("staxTime", Long.toString(staxCrawler.getDuration()));
        request.setAttribute("staxAverageTime", (staxCrawler.getDuration()/staxCrawler.getNumberFoundProducts()));
        return staxCrawler;
    }
}
