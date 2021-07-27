package sync.services.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.models.Row;
import sync.models.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableBuilder {

    private static final Logger logger = LoggerFactory.getLogger(TableBuilder.class);

    public static Table getTable(ResultSet resultSet){
        Table table = new Table();
        try {
            List<String> columns = new ArrayList<>();
            int columnsCount = resultSet.getMetaData().getColumnCount();
            for(int i = 1; i <= columnsCount; i++){
                columns.add(resultSet.getMetaData().getColumnName(i));
            }
            Row columnsRow = new Row(0, columns);
            table.getRows().add(columnsRow);

            while (resultSet.next()){
                Integer id = -1;
                List<String> values = new ArrayList<>();
                for(int i = 1; i<= columnsCount; i++){
                    if(resultSet.getMetaData().getColumnName(i).equals("ID")){
                        id = resultSet.getInt(i);
                    } else {
                        values.add(resultSet.getString(i));
                    }
                }
                Row row = new Row(id, values);
                table.getRows().add(row);
            }
        } catch (SQLException ex){
            logger.error("getTable: "+ex.getMessage());
        }
        return table;
    }
}
