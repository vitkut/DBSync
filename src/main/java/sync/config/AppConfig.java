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
            String url = propertiesReader.getProperty("db.slave.url");
            String username = propertiesReader.getProperty("db.slave.username");
            String password = propertiesReader.getProperty("db.slave.password");
            String tableName = propertiesReader.getProperty("db.slave.tableName");
            DataSource dataSource = new DriverManagerDataSource(url, username, password);
            Connection connection = dataSource.getConnection();
            if(connection.isClosed()){
                throw new SQLException("Can not connect to DB: "+url);
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
            String url = propertiesReader.getProperty("db.master.url");
            String username = propertiesReader.getProperty("db.master.username");
            String password = propertiesReader.getProperty("db.master.password");
            String tableName = propertiesReader.getProperty("db.master.tableName");
            DataSource dataSource = new DriverManagerDataSource(url, username, password);
            Connection connection = dataSource.getConnection();
            if(connection.isClosed()){
                throw new SQLException("Can not connect to DB: "+url);
            }
            MasterDatabase masterDatabase = new MasterDatabase(connection, tableName);
            logger.debug("getMasterDatabase -> "+masterDatabase);
            return masterDatabase;
        } catch (Exception e){
            logger.error("getMasterDatabase: "+e.getMessage());
            throw e;
        }

    }

    public AdapterDatabase getAdapterDatabase() throws SQLException{
        try {
            logger.debug("getAdapterDatabase()");
            StringBuilder url = new StringBuilder(propertiesReader.getProperty("db.adapter.url"));
            String username = propertiesReader.getProperty("db.adapter.username");
            String password = propertiesReader.getProperty("db.adapter.password");
            String tableName = propertiesReader.getProperty("db.adapter.tableName");
            String databaseName = propertiesReader.getProperty("db.adapter.databaseName");
            DataSource tempDataSource = new DriverManagerDataSource(url.toString(), username, password);
            Connection tempConnection = tempDataSource.getConnection();
            if(tempConnection.isClosed()){
                throw new SQLException("Can not connect to DB: "+url.toString());
            }
            ResultSet resultSet = tempConnection.createStatement().executeQuery("show databases");
            boolean needToCreateDatabase = true;
            while(resultSet.next()){
                if(resultSet.getString(1).equals(databaseName)){
                    needToCreateDatabase = false;
                    break;
                }
            }
            if(needToCreateDatabase){
                logger.debug("Creating adapter database");
                tempConnection.createStatement().execute("create database "+databaseName);
            }
            url.append("/").append(databaseName);
            DataSource dataSource = new DriverManagerDataSource(url.toString(), username, password);
            Connection connection = dataSource.getConnection();
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
                logger.debug("Creating table in adapter database");
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

    public boolean syncSameColumns(){
        String bool = propertiesReader.getProperty("syncSameColumns");
        if(bool.equals("true")){
            return true;
        } else {
            return false;
        }
    }

    public boolean initColumnsToFile(){
        String bool = propertiesReader.getProperty("initColumnsToFile");
        if(bool.equals("true")){
            propertiesReader.setProperty("initColumnsToFile", "false");
            return true;
        } else {
            return false;
        }
    }

    public boolean convertColumns(){
        String bool = propertiesReader.getProperty("convertColumns");
        if(bool.equals("true")){
            return true;
        } else {
            return false;
        }
    }
}
