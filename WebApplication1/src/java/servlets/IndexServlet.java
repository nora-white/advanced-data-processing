package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import businesslogic.StAXCrawler;
import businesslogic.JSONBuilder;
import domain.Product;
import java.util.ArrayList;
//import businesslogic.DOMCrawler;
import businesslogic.DOMScraper;
import businesslogic.StAXScraper;
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
        request.setAttribute("inputBrand", inputBrand);
        request.setAttribute("inputProduct", inputProduct);
        request.setAttribute("inputURL", inputURL);
        ArrayList<Product> scrapedProducts = new ArrayList<>();
        
        // Read in saved products from JSON
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.getJsonFromFile();
        ArrayList<Product> jsonProducts = jsonBuilder.getJsonProductList();
        request.setAttribute("products", jsonProducts);
            
        // Run StAX crawler
        StAXCrawler staxCrawler = new StAXCrawler(inputBrand.toLowerCase(), inputProduct.toLowerCase(), inputURL);
        staxCrawler.search();
        ArrayList<String> foundProductURLs = staxCrawler.getFoundProducts();
        request.setAttribute("staxTotalResults", staxCrawler.getNumberFoundProducts());
        request.setAttribute("staxTime", staxCrawler.getDuration());
        
        // Find product info and time how long it takes
        long scraperStartTime = System.currentTimeMillis();
        for (int i = 0; i < foundProductURLs.size(); i++) {
            DOMScraper domScraper = new DOMScraper(foundProductURLs.get(i), inputBrand.toLowerCase(), staxCrawler.getCrawlDelay());
            if (domScraper.getProduct() != null)
                scrapedProducts.add(domScraper.getProduct());
        }
        long scraperEndTime = System.currentTimeMillis();
        request.setAttribute("domScraperDuration", Long.toString(TimeUnit.MILLISECONDS.toSeconds(scraperEndTime - scraperStartTime)) + " seconds");

        // Set products
        request.setAttribute("scrapedProducts", scrapedProducts);
        
//        StAXScraper staxScraper = new StAXScraper(staxCrawler.getFoundProducts(), staxCrawler.getCrawlDelay());
//        DOMCrawler domCrawler = new DOMCrawler(inputBrand, inputProduct);
//        domCrawler.search();
        
        request.setAttribute("searchCompleted", true);
        

//        request.setAttribute("scraperResults", domScraper.getResults());
        request.setAttribute("sitemapURL", staxCrawler.getBaseSitemapURL());
        request.setAttribute("crawlDelay", staxCrawler.getCrawlDelay());

//        request.setAttribute("domTime", "<b>DOM search duration: </b>" + domCrawler.getDuration() + " seconds");
        
//        request.setAttribute("domFoundProducts", "<b>DOM found products: (" + domCrawler.getNumberFoundProducts() + "): </b>" + domCrawler.getFoundProducts());

        getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);  

    }
}
