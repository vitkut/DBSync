package sync.models;

import sync.models.primary.Database;

import java.sql.Connection;

public class AdapterDatabase extends Database {

    public AdapterDatabase(Connection connection, String tableName) {
        super(connection, tableName);
    }
}
