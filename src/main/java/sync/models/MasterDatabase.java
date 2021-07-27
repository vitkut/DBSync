package sync.models;

import sync.models.primary.Database;

import java.sql.Connection;

public class MasterDatabase extends Database {

    public MasterDatabase(Connection connection, String tableName) {
        super(connection, tableName);
    }

}
