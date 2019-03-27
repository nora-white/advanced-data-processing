package businesslogic;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
public class JSONBuilder {
    
    private boolean jsonIsNull;

    public JSONBuilder() {
        JsonParser parser = new JsonParser();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources/foundproducts.json");
        Reader reader = new InputStreamReader(inputStream);
        JsonElement rootElement = parser.parse(reader);
        
        if (rootElement.isJsonNull()) {
            jsonIsNull = false;
        } else {
            jsonIsNull = true;
        }
    }
    
    public boolean isJsonNull() {
        return jsonIsNull;
    }
}
