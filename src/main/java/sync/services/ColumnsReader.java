package sync.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.models.Row;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ColumnsReader {

    private static final Logger logger = LoggerFactory.getLogger(ColumnsReader.class);
    private static List<Row> columns;

    public static Row getMasterColumns(){
        return getColumns().get(0);
    }

    public static Row getSlaveColumns(){
        return getColumns().get(1);
    }

    public static List<Row> getColumns(){
        if(columns == null){
            initColumns();
        }
        return columns;
    }

    private static void initColumns(){
        try {
            PropertiesReader propertiesReader = new PropertiesReader("columns.properties");
            List<String> keys = propertiesReader.getKeys();
            List<String> masterColumns = new ArrayList<>();
            List<String> slaveColumns = new ArrayList<>();
            for(String s:keys){
                String v = propertiesReader.getProperty(s);
                if(!v.equals("")){
                    masterColumns.add(s);
                    slaveColumns.add(v);
                }
            }
            Row masterRow = new Row(0, masterColumns);
            Row slaveRow = new Row(1, slaveColumns);
            List<Row> rows = new ArrayList<>();
            rows.add(masterRow);
            rows.add(slaveRow);
            columns = rows;
        } catch (IOException ex){
            logger.error(ex.getMessage());
            columns = new ArrayList<>();
        }
    }
}
