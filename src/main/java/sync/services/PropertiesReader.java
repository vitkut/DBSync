package sync.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    private Properties properties = new Properties();

    public PropertiesReader()throws IOException {
        this("./src/main/resources/application.properties");
    }

    public PropertiesReader(String propertiesPath) throws IOException {
        File file = new File(propertiesPath);
        properties.load(new FileInputStream(file));
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }
}
