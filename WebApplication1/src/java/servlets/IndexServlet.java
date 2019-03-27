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
import businesslogic.DOMCrawler;
import businesslogic.StAXScraper;

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
        
        String inputBrand = request.getParameter("inputBrand");
        String inputProduct = request.getParameter("inputProduct");
        
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.getJsonFromFile();
        ArrayList<Product> jsonProducts = jsonBuilder.getJsonProductList();
            
        StAXCrawler staxCrawler = new StAXCrawler(inputBrand, inputProduct);
        staxCrawler.search();
        
        StAXScraper staxScraper = new StAXScraper(staxCrawler.getFoundProducts(), staxCrawler.getCrawlDelay());

//        DOMCrawler domCrawler = new DOMCrawler(inputBrand, inputProduct);
//        domCrawler.search();
        
        request.setAttribute("searchCompleted", true);
        request.setAttribute("inputBrand", inputBrand);
        request.setAttribute("inputProduct", inputProduct);
        request.setAttribute("products", jsonProducts);

        request.setAttribute("scraperResults", staxScraper.getResults());
        request.setAttribute("sitemapURL", staxCrawler.getSitemapURL());
        request.setAttribute("crawlDelay", staxCrawler.getCrawlDelay());
        request.setAttribute("staxTotalResults", staxCrawler.getNumberFoundProducts());
        request.setAttribute("staxTime", staxCrawler.getDuration());
//        request.setAttribute("domTime", "<b>DOM search duration: </b>" + domCrawler.getDuration() + " seconds");
        
//        request.setAttribute("domFoundProducts", "<b>DOM found products: (" + domCrawler.getNumberFoundProducts() + "): </b>" + domCrawler.getFoundProducts());

        getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);  

    }
}
