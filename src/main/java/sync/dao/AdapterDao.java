package sync.dao;

import sync.models.Table;

import java.sql.SQLException;
import java.util.List;

public interface AdapterDao {

    Table getAll() throws SQLException;
    void put(Integer masterId, Integer slaveId) throws SQLException;
    List<Integer> get(Integer masterId) throws SQLException;
    void removeBySlave(Integer slaveId) throws SQLException;
    void removeByMaster(Integer masterId) throws SQLException;
    boolean contains(Integer masterId, Integer slaveId) throws SQLException;
    String getTableName();
}
