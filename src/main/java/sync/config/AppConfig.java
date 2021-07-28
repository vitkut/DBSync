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
        try {
            logger.debug("getSlaveDatabase()");
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
            SlaveDatabase slaveDatabase = new SlaveDatabase(connection, tableName);
            logger.debug("getSalveDatabase -> "+slaveDatabase);
            return slaveDatabase;
        } catch (Exception ex){
            logger.error("getSlaveDatabase: "+ex.getMessage());
            throw ex;
        }
    }

    public MasterDatabase getMasterDatabase() throws SQLException{
        try {
            logger.debug("getMasterDatabase()");
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
            MasterDatabase masterDatabase = new MasterDatabase(connection, tableName);
            logger.debug("getMasterDatabase -> "+masterDatabase);
            return masterDatabase;
        } catch (Exception e){
            logger.error("getMasterDatabase: "+e.getMessage());
            throw e;
        }

    }

    public AdapterDatabase getAdapterDatabase(MasterDatabase masterDatabase) throws SQLException{
        try {
            logger.debug("getAdapterDatabase()");
            StringBuilder url = new StringBuilder();
            url.append("jdbc:mysql://").append(propertiesReader.getProperty("db.master.host"))
                    .append(":").append(propertiesReader.getProperty("db.master.port"))
                    .append("/").append(propertiesReader.getProperty("db.adapter.databaseName"));
            String username = propertiesReader.getProperty("db.master.username");
            String password = propertiesReader.getProperty("db.master.password");
            String tableName = propertiesReader.getProperty("db.adapter.tableName");
            DataSource dataSource = new DriverManagerDataSource(url.toString(), username, password);
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
            Connection connection = dataSource.getConnection();
            if(connection.isClosed()){
                throw new SQLException("Can not connect to DB: "+url.toString());
            }
            AdapterDatabase adapterDatabase = new AdapterDatabase(connection, tableName);
            boolean needToCreateTable = true;
            resultSet = adapterDatabase.getConnection().createStatement().executeQuery("show tables");
            while(resultSet.next()){
                if(resultSet.getString(1).equals(tableName)){
                    needToCreateTable = false;
                    break;
                }
            }
            if(needToCreateTable){
                adapterDatabase.getConnection().createStatement()
                        .execute("create table "+tableName
                                +" (master_id int, slave_id int)");
            }
            logger.debug("getAdapterDatabase -> " + adapterDatabase);
            return adapterDatabase;
        } catch (Exception e){
            logger.error("getAdapterDatabase: "+e.getMessage());
            throw e;
        }
    }

    public Integer getPeriod() {
        logger.debug("getPeriod()");
        Integer period = 1000*Integer.parseInt(propertiesReader.getProperty("period"));;
        logger.debug("getPeriod -> "+period);
        return period;
    }

    public String getDumpPath(){
        logger.debug("getDumpPath()");
        String dumpPath = propertiesReader.getProperty("dumpPath");;
        logger.debug("getDumpPath -> "+dumpPath);
        return dumpPath;
    }
}
