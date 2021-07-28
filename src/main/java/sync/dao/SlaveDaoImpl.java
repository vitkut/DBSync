package sync.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.models.ChangeLog;
import sync.models.Row;
import sync.models.SlaveDatabase;
import sync.models.Table;
import sync.services.utils.SQLBuilder;
import sync.services.utils.TableBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SlaveDaoImpl implements SlaveDao {

    private SlaveDatabase slaveDatabase;
    private Row columns;
    private static final Logger logger = LoggerFactory.getLogger(SlaveDaoImpl.class);

    public SlaveDaoImpl(SlaveDatabase slaveDatabase, Row columns) {
        this.slaveDatabase = slaveDatabase;
        this.columns = columns;
    }

    @Override
    public Table getAll() throws SQLException {
        logger.debug("getAll()");
        Table table = TableBuilder
                .getTable(slaveDatabase.getConnection().createStatement()
                        .executeQuery(SQLBuilder.getSelectSQL(columns, slaveDatabase.getTableName())));
        logger.debug("getAll -> "+table);
        return table;
    }

    @Override
    public void update(ChangeLog changeLog) throws SQLException {
        logger.debug("update("+changeLog+")");
        slaveDatabase.getConnection().createStatement()
                .execute(SQLBuilder.getUpdateSQL(slaveDatabase.getTableName(), changeLog.getColumns(),
                         changeLog.getFrom(), changeLog.getTo()));
        logger.debug("update done");
    }

    @Override
    public void delete(ChangeLog changeLog) throws SQLException {
        logger.debug("delete("+changeLog+")");
        slaveDatabase.getConnection().createStatement()
                .execute(SQLBuilder.getDeleteSQL(slaveDatabase.getTableName(), changeLog.getColumns(),
                        changeLog.getFrom()));
        logger.debug("delete done");
    }

    @Override
    public Integer add(ChangeLog changeLog) throws SQLException {
        logger.debug("add("+changeLog+")");
        String sqlAdd = SQLBuilder.getAddSQL(slaveDatabase.getTableName(), changeLog.getColumns(), changeLog.getTo());
        String sqlGetId = SQLBuilder.getId(slaveDatabase.getTableName(), changeLog.getColumns(), changeLog.getTo());
        slaveDatabase.getConnection().createStatement().execute(sqlAdd);
        ResultSet resultSet = slaveDatabase.getConnection().createStatement().executeQuery(sqlGetId);
        Table table = TableBuilder.getTable(resultSet);
        Integer id = -1;
        if(!table.getRows().isEmpty()){
            id = table.getRows().get(table.getRows().size()-1).getId();
        }
        logger.debug("add -> "+id);
        return id;
    }

    public SlaveDatabase getSlaveDatabase() {
        return slaveDatabase;
    }

    public void setSlaveDatabase(SlaveDatabase slaveDatabase) {
        this.slaveDatabase = slaveDatabase;
    }

    public Row getColumns() {
        return columns;
    }

    public void setColumns(Row columns) {
        this.columns = columns;
    }
}
