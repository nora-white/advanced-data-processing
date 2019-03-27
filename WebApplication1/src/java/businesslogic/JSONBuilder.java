package businesslogic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import domain.Product;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
public class JSONBuilder {
    
    ArrayList<Product> jsonProductList = new ArrayList<>();

    public JSONBuilder() {
        // Do Nothing
    }
    
    public void getJsonFromFile() {
        // Get the parser, input stream, and reader ready
        JsonParser parser = new JsonParser();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources/foundproducts.json");
        Reader reader = new InputStreamReader(inputStream);
        
        // Begin accessing the json file
        JsonElement rootElement = parser.parse(reader);
        JsonArray jsonArray = rootElement.getAsJsonArray();
        JsonObject jsonObject;
        
        // DateTimeFormatter created so that we can parse the String to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.get(i).getAsJsonObject();
            Product product = new Product(
                LocalDateTime.parse(jsonObject.get("timestamp").getAsString(), formatter), 
                jsonObject.get("brand").getAsString(), 
                jsonObject.get("name").getAsString(),
                jsonObject.get("sizeoz").getAsString(),
                jsonObject.get("sizeg").getAsString(),
                jsonObject.get("price").getAsFloat(),
                jsonObject.get("imgurl").getAsString(),
                jsonObject.get("producturl").getAsString()
            );
            jsonProductList.add(product);
        }
    }
    
    public ArrayList<Product> getJsonProductList() {
        return jsonProductList;
    }
    
    public void writeJSON() {
             
        
    }
    
}
