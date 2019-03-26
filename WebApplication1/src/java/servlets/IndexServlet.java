package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import businesslogic.StAXCrawler;
import businesslogic.DOMCrawler;

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
            
        StAXCrawler staxCrawler = new StAXCrawler(inputBrand, inputProduct);
        staxCrawler.search();
        
        DOMCrawler domCrawler = new DOMCrawler(inputBrand, inputProduct);
        domCrawler.search();
        
        request.setAttribute("inputBrand", inputBrand);
        request.setAttribute("inputProduct", inputProduct);

        request.setAttribute("result", "<b>Disallowed pages: </b>" + staxCrawler.getDisallowedPages());
        request.setAttribute("sitemapURL", "<b>Sitemap URL: </b>" + staxCrawler.getSitemapURL());
        request.setAttribute("crawlDelay", "<b>Crawl-delay: </b>" + staxCrawler.getCrawlDelay());
        request.setAttribute("domTime", "<b>DOM search duration: </b>" + domCrawler.getDuration() + " seconds");
        request.setAttribute("staxTime", "<b>StAX search duration: </b>" + staxCrawler.getDuration() + " seconds");
        request.setAttribute("domFoundProducts", "<b>DOM found products: (" + domCrawler.getNumberFoundProducts() + "): </b>" + domCrawler.getFoundProducts());
        request.setAttribute("staxFoundProducts", "<b>StAX found products: (" + staxCrawler.getNumberFoundProducts() + "): </b>" + staxCrawler.getFoundProducts());
        getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);  

    }
}
