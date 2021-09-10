package sync.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.models.Row;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConverterReader {

    private static List<Row> covertation;
    private static final Logger logger = LoggerFactory.getLogger(ConverterReader.class);

    public static List<Row> getCovertation() throws IOException {
        if(covertation == null){
            initConvertation();
        }
        return covertation;
    }

    private static void initConvertation() throws IOException{
        try {
            covertation = new ArrayList<>();
            PropertiesReader propertiesReader = new PropertiesReader("convert.properties");
            List<String> masterColumns = propertiesReader.getKeys();
            List<String> convertColumns = new ArrayList<>();
            List<String> convertMethod = new ArrayList<>();
            for(String c:masterColumns){
                String method = propertiesReader.getProperty(c);
                if(!method.equals("")){
                    convertColumns.add(c);
                    convertMethod.add(method);
                }
            }
            covertation.add(new Row(0, convertColumns));
            covertation.add(new Row(1, convertMethod));
            logger.debug("Convertation: "+covertation);
        } catch (IOException ex){
            logger.error(ex.getMessage());
            throw ex;
        }
    }
}
