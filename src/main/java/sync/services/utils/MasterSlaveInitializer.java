package sync.services.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.models.MasterDatabase;
import sync.models.Row;
import sync.models.SlaveDatabase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MasterSlaveInitializer {

    private static final Logger logger = LoggerFactory.getLogger(MasterSlaveInitializer.class);

    //rewrite later
    public static Row getSameColumns(MasterDatabase masterDatabase, SlaveDatabase slaveDatabase) throws SQLException {
        try {
            logger.debug("getSameColumns("+masterDatabase+", "+slaveDatabase+")");
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
            sameColumns.remove("ID");
            Row row = new Row(0, sameColumns);
            logger.debug("getSameColumns -> "+row);
            return row;
        } catch (Exception ex){
            logger.error("getSameColumns: "+ex.getMessage());
            throw ex;
        }
    }

    public static void initColumns(MasterDatabase masterDatabase, SlaveDatabase slaveDatabase) throws SQLException {
        List<String> masterColumns = new ArrayList<>();
        List<String> slaveColumns = new ArrayList<>();
        String sqlMaster = "desc " + masterDatabase.getTableName();
        String sqlSlave = "desc " + slaveDatabase.getTableName();
        ResultSet resultSet = masterDatabase.getConnection().createStatement().executeQuery(sqlMaster);
        while (resultSet.next()) {
            masterColumns.add(resultSet.getString(1));
        }
        resultSet = slaveDatabase.getConnection().createStatement().executeQuery(sqlSlave);
        while (resultSet.next()) {
            slaveColumns.add(resultSet.getString(1));
        }
        File file = new File("columns.properties");
        try (FileWriter fileWriter = new FileWriter(file, false)){
            for (String column:masterColumns){
                fileWriter.write(column+"=\n");
            }
            fileWriter.write("\n#---SLAVE COLUMNS---\n\n");
            for(String slaveColumn:slaveColumns){
                fileWriter.write("#"+slaveColumn+"\n");
            }
        } catch (IOException ex){
            logger.error("Can't write to columns.properties ", ex);
        }
    }
}
