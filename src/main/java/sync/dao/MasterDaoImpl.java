package sync.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.models.MasterDatabase;
import sync.models.Row;
import sync.models.Table;
import sync.services.utils.SQLBuilder;
import sync.services.utils.TableBuilder;

import java.sql.SQLException;

public class MasterDaoImpl implements MasterDao {

    private MasterDatabase masterDatabase;
    private Row columns;
    private static final Logger logger = LoggerFactory.getLogger(MasterDaoImpl.class);

    public MasterDaoImpl(MasterDatabase masterDatabase, Row columns) {
        this.masterDatabase = masterDatabase;
        this.columns = columns;
    }

    @Override
    public Table getAll() throws SQLException {
        logger.debug("getAll()");
        Table table = TableBuilder
                .getTable(masterDatabase.getConnection().createStatement()
                        .executeQuery(SQLBuilder.getSelectSQL(columns, masterDatabase.getTableName())));
        logger.debug("getAll -> "+table);
        return table;
    }

    public MasterDatabase getMasterDatabase() {
        return masterDatabase;
    }

    public void setMasterDatabase(MasterDatabase masterDatabase) {
        this.masterDatabase = masterDatabase;
    }

    public Row getColumns() {
        return columns;
    }

    public void setColumns(Row columns) {
        this.columns = columns;
    }
}
