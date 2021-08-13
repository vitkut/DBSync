package databases.entity;

import java.sql.Connection;
import java.util.List;

public class MasterDatabase extends Database {

    public MasterDatabase(Connection connection, String tableName, List<String> columnNames) {
        super(connection, tableName, columnNames);
    }
}
