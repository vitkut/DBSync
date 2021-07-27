package sync.models;

import sync.models.primary.Database;

import java.sql.Connection;

public class SlaveDatabase extends Database {

    public SlaveDatabase(Connection connection, String tableName) {
        super(connection, tableName);
    }
}
