package sync.dao;

import sync.models.Table;

import java.sql.SQLException;

public interface MasterDao {

    Table getAll() throws SQLException;
}
