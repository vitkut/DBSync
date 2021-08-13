package databases.entity;

import java.sql.Connection;
import java.util.List;

public class SlaveDatabase extends Database{

    public SlaveDatabase(Connection connection, String tableName, List<String> columnNames) {
        super(connection, tableName, columnNames);
    }
}
