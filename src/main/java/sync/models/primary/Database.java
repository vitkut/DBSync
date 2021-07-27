package sync.models.primary;

import java.sql.Connection;

public abstract class Database {

    private Connection connection;
    private String tableName;

    public Database(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "Database{" +
                "tableName='" + tableName + '\'' +
                '}';
    }
}
