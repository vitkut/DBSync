package databases.entity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public abstract class Database {

    private final Connection connection;
    private final String tableName;
    private final List<String> columnNames;

    public Database(Connection connection, String tableName, List<String> columnNames) {
        this.connection = connection;
        this.tableName = tableName;
        this.columnNames = columnNames;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumnNames() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Database{" +
                "connection=" + connection +
                ", tableName='" + tableName + '\'' +
                ", columnNames=" + columnNames +
                '}';
    }
}
