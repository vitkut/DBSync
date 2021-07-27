package sync.services.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.models.MasterDatabase;
import sync.models.Row;
import sync.models.SlaveDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MasterSlaveInitializer {

    private static final Logger logger = LoggerFactory.getLogger(MasterSlaveInitializer.class);

    //rewrite later
    public static Row getSameColumns(MasterDatabase masterDatabase, SlaveDatabase slaveDatabase) throws SQLException {
        try {
            List<String> columns1 = new ArrayList<>();
            List<String> columns2 = new ArrayList<>();
            List<String> sameColumns = new ArrayList<>();
            String sql1 = "desc "+masterDatabase.getTableName();
            String sql2 = "desc "+slaveDatabase.getTableName();
            ResultSet resultSet = masterDatabase.getConnection().createStatement().executeQuery(sql1);
            while (resultSet.next()){
                columns1.add(resultSet.getString(1));
            }
            resultSet = slaveDatabase.getConnection().createStatement().executeQuery(sql2);
            while (resultSet.next()){
                columns2.add(resultSet.getString(1));
            }
            columns1.stream().filter(columns2::contains).forEach(sameColumns::add);
            if(!sameColumns.contains("ID")){
                throw new SQLException("Wrong main table [has not column ID]");
            }
            return new Row(0, sameColumns);
        } catch (SQLException ex){
            logger.error("getSameColumns: "+ex.getMessage());
            throw ex;
        }
    }
}
