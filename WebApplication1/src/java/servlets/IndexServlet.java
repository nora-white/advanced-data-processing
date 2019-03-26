package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import businesslogic.Crawler;

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
            
        Crawler crawler = new Crawler(inputBrand, inputProduct);
        
        request.setAttribute("inputBrand", inputBrand);
        request.setAttribute("inputProduct", inputProduct);

        request.setAttribute("siteMapSites", "<b>Sitemap site: </b>" + crawler.searchForProductPage());
        request.setAttribute("result", "<b>Disallowed pages: </b>" + crawler.getDisallowedPages());
        request.setAttribute("sitemapURL", "<b>Sitemap URL: </b>" + crawler.getSitemapURL());
        request.setAttribute("crawlDelay", "<b>Crawl-delay: </b>" + crawler.getCrawlDelay());
        request.setAttribute("allProducts", "<b>All products: </b>" + crawler.getAllProducts());
        getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);  

    }
}
