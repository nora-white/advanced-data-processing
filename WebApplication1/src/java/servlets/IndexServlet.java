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
        
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        String[] splitLine;
        
        ArrayList<String> disallowedPages = new ArrayList<String>(); 
        
        try {
            url = new URL("https://www.sephora.com/robots.txt");
            is = url.openStream();
            br = new BufferedReader(new InputStreamReader(is));
            
            while ((line = br.readLine()) != null) {
                if (line.contains("Disallow")) {
                    splitLine = line.split(": ");
                    disallowedPages.add(splitLine[1]);
                }
            }
            request.setAttribute("inputBrand", inputBrand);
            request.setAttribute("inputProduct", inputProduct);
            request.setAttribute("result", "<b>Disallowed pages: </b>" + disallowedPages.toString());
            getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);  
        } catch (MalformedURLException mue) {
         mue.printStackTrace();
        } catch (IOException ioe) {
             ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
    }
}
