package sync.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import sync.models.AdapterDatabase;
import sync.models.MasterDatabase;
import sync.models.SlaveDatabase;
import sync.services.PropertiesReader;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppConfig {

    private PropertiesReader propertiesReader;
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    public AppConfig(PropertiesReader props) {
        this.propertiesReader = props;
    }

    public SlaveDatabase getSlaveDatabase() throws SQLException{
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://").append(propertiesReader.getProperty("db.host"))
                .append(":").append(propertiesReader.getProperty("db.port"))
                .append("/").append(propertiesReader.getProperty("db.databaseName"));
        String username = propertiesReader.getProperty("db.username");
        String password = propertiesReader.getProperty("db.password");
        String tableName = propertiesReader.getProperty("db.tableName");
        DataSource dataSource = new DriverManagerDataSource(url.toString(), username, password);
        Connection connection = dataSource.getConnection();
        if(connection.isClosed()){
            throw new SQLException("Can not connect to DB: "+url.toString());
        }
        return new SlaveDatabase(connection, tableName);
    }

    public MasterDatabase getMasterDatabase() throws SQLException{
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://").append(propertiesReader.getProperty("db.master.host"))
                .append(":").append(propertiesReader.getProperty("db.master.port"))
                .append("/").append(propertiesReader.getProperty("db.master.databaseName"));
        String username = propertiesReader.getProperty("db.master.username");
        String password = propertiesReader.getProperty("db.master.password");
        String tableName = propertiesReader.getProperty("db.master.tableName");
        DataSource dataSource = new DriverManagerDataSource(url.toString(), username, password);
        Connection connection = dataSource.getConnection();
        if(connection.isClosed()){
            throw new SQLException("Can not connect to DB: "+url.toString());
        }
        return new MasterDatabase(connection, tableName);
    }

    public AdapterDatabase getAdapterDatabase(MasterDatabase masterDatabase) throws SQLException{
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://").append(propertiesReader.getProperty("db.master.host"))
                .append(":").append(propertiesReader.getProperty("db.master.port"))
                .append("/").append(propertiesReader.getProperty("db.adapter.databaseName"));
        String username = propertiesReader.getProperty("db.master.username");
        String password = propertiesReader.getProperty("db.master.password");
        String tableName = propertiesReader.getProperty("db.adapter.tableName");
        DataSource dataSource = new DriverManagerDataSource(url.toString(), username, password);
        try {
            ResultSet resultSet = masterDatabase.getConnection().createStatement().executeQuery("show databases");
            boolean needToCreateDatabase = true;
            while(resultSet.next()){
                if(resultSet.getString(1).equals(propertiesReader.getProperty("db.adapter.databaseName"))){
                    needToCreateDatabase = false;
                    break;
                }
            }
            if(needToCreateDatabase){
                masterDatabase.getConnection().createStatement().execute("create database "+propertiesReader.getProperty("db.adapter.databaseName"));
            }
            boolean needToCreateTable = true;
            resultSet = masterDatabase.getConnection().createStatement().executeQuery("show tables");
            while(resultSet.next()){
                if(resultSet.getString(1).equals(propertiesReader.getProperty("db.adapter.tableName"))){
                    needToCreateTable = false;
                    break;
                }
            }
            if(needToCreateTable){
                masterDatabase.getConnection().createStatement()
                        .execute("create table "+propertiesReader.getProperty("db.adapter.tableName")
                                +" (master_id int, slave_id int)");
            }
            Connection connection = dataSource.getConnection();
            if(connection.isClosed()){
                throw new SQLException("Can not connect to DB: "+url.toString());
            }
            return new AdapterDatabase(connection, tableName);
        } catch (SQLException e){
            logger.error("getAdapterDatabase: "+e.getMessage());
            throw e;
        }
    }

    public Integer getPeriod() {
        return 1000*Integer.parseInt(propertiesReader.getProperty("period"));
    }

    public String getDumpPath(){
        return propertiesReader.getProperty("dumpPath");
    }
}
