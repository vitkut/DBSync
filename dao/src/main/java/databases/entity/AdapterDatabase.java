package databases.entity;

import java.sql.Connection;
import java.util.List;

public class AdapterDatabase extends Database {

    public AdapterDatabase(Connection connection, String tableName, List<String> columnNames) {
        super(connection, tableName, columnNames);
    }
}
