package sync.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesReader {

    private Properties properties = new Properties();

    public PropertiesReader()throws IOException {
        this("application.properties");
    }

    public PropertiesReader(String propertiesPath) throws IOException {
        File file = new File(propertiesPath);
        properties.load(new FileInputStream(file));
    }

    public String getProperty(String key){
        return properties.getProperty(key, "");
    }

    public List<String> getKeys() {
        return new ArrayList<>(properties.stringPropertyNames());
    }

    public void setProperty(String key, String value){
        properties.setProperty(key, value);
    }
}
