package databases.dao;

import common.entity.Row;
import databases.entity.AdapterDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class AdapterDao {

    private static final Logger logger = LoggerFactory.getLogger(AdapterDao.class);
    private AdapterDatabase adapterDatabase;

    public AdapterDao(AdapterDatabase adapterDatabase) {
        this.adapterDatabase = adapterDatabase;
    }

    public void add(Row row){
        if(!contains(row)){
            try {
                adapterDatabase.getConnection().createStatement().execute()
            } catch (SQLException ex){
                logger.error(ex.getMessage());
            }
        }
    }

    public Row getByKey(String key){

    }

    public boolean contains(Row row){

    }

    public void remove(Row row){

    }

    public void clear(){

    }
}
