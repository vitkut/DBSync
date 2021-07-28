package sync.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.models.AdapterDatabase;
import sync.models.Table;
import sync.services.utils.TableBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdapterDaoImpl implements AdapterDao {

    private AdapterDatabase adapterDatabase;
    private static final Logger logger = LoggerFactory.getLogger(AdapterDaoImpl.class);

    public AdapterDaoImpl(AdapterDatabase adapterDatabase) {
        this.adapterDatabase = adapterDatabase;
    }

    @Override
    public Table getAll() throws SQLException {
        logger.debug("getAll()");
        Table table = TableBuilder.getTable(adapterDatabase.getConnection().createStatement()
                .executeQuery("select * from "+adapterDatabase.getTableName()));
        logger.debug("getAll -> "+table);
        return table;
    }

    @Override
    public void put(Integer masterId, Integer slaveId) throws SQLException {
        logger.debug("put("+masterId+", "+slaveId+")");
        adapterDatabase.getConnection().createStatement()
                .execute("insert into "+adapterDatabase.getTableName()+" values ('"+masterId+"', '"+slaveId+"')");
        logger.debug("put done");
    }

    @Override
    public List<Integer> get(Integer masterId) throws SQLException {
        logger.debug("get("+masterId+")");
        ResultSet resultSet = adapterDatabase.getConnection().createStatement()
                .executeQuery("select slave_id from "+adapterDatabase.getTableName()+" where master_id='"+masterId+"'");
        List<Integer> slaveIdList = new ArrayList<>();
        while (resultSet.next()){
            slaveIdList.add(resultSet.getInt(1));
        }
        logger.debug("get -> "+slaveIdList);
        return slaveIdList;
    }

    @Override
    public void removeBySlave(Integer slaveId) throws SQLException {
        logger.debug("removeBySlave("+slaveId+")");
        adapterDatabase.getConnection().createStatement()
                .execute("delete from "+adapterDatabase.getTableName()+" where slave_id='"+slaveId+"'");
        logger.debug("removeBySlave done");
    }

    @Override
    public void removeByMaster(Integer masterId) throws SQLException {
        logger.debug("removeByMaster("+masterId+")");
        adapterDatabase.getConnection().createStatement()
                .execute("delete from "+adapterDatabase.getTableName()+" where master_id='"+masterId+"'");
        logger.debug("removeByMaster done");
    }

    @Override
    public boolean contains(Integer masterId, Integer slaveId) throws SQLException{
        logger.debug("contains("+masterId+", "+slaveId+")");
        List<Integer> slaveIdList = get(masterId);
        if(slaveIdList.contains(slaveId)){
            logger.debug("contains -> true");
            return true;
        } else {
            logger.debug("contains -> false");
            return false;
        }
    }

    @Override
    public String getTableName() {
        logger.debug("getTableName()");
        String name = adapterDatabase.getTableName();
        logger.debug("getTableName -> "+name);
        return name;
    }

    public AdapterDatabase getAdapterDatabase() {
        return adapterDatabase;
    }

    public void setAdapterDatabase(AdapterDatabase adapterDatabase) {
        this.adapterDatabase = adapterDatabase;
    }
}
