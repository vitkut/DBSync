package sync.dao;

import sync.models.ChangeLog;
import sync.models.Row;
import sync.models.Table;

import java.sql.SQLException;

public interface SlaveDao {

    Table getAll() throws SQLException;
    void update(ChangeLog changeLog) throws SQLException;
    void delete(ChangeLog changeLog) throws SQLException;
    Integer add(ChangeLog changeLog) throws SQLException;
}
