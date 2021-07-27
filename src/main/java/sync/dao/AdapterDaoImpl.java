package sync.dao;

import sync.models.AdapterDatabase;
import sync.models.Table;
import sync.services.utils.TableBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdapterDaoImpl implements AdapterDao {

    private AdapterDatabase adapterDatabase;

    public AdapterDaoImpl(AdapterDatabase adapterDatabase) {
        this.adapterDatabase = adapterDatabase;
    }

    @Override
    public Table getAll() throws SQLException {
        return TableBuilder.getTable(adapterDatabase.getConnection().createStatement()
                .executeQuery("select * from "+adapterDatabase.getTableName()));
    }

    @Override
    public void put(Integer masterId, Integer slaveId) throws SQLException {
        adapterDatabase.getConnection().createStatement()
                .execute("insert into "+adapterDatabase.getTableName()+" values ('"+masterId+"', '"+slaveId+"')");
    }

    @Override
    public List<Integer> get(Integer masterId) throws SQLException {
        ResultSet resultSet = adapterDatabase.getConnection().createStatement()
                .executeQuery("select slave_id from "+adapterDatabase.getTableName()+" where master_id='"+masterId+"'");
        List<Integer> slaveIdList = new ArrayList<>();
        while (resultSet.next()){
            slaveIdList.add(resultSet.getInt(1));
        }
        return slaveIdList;
    }

    @Override
    public void removeBySlave(Integer slaveId) throws SQLException {
        adapterDatabase.getConnection().createStatement()
                .execute("delete from "+adapterDatabase.getTableName()+" where slave_id='"+slaveId+"'");
    }

    @Override
    public void removeByMaster(Integer masterId) throws SQLException {
        adapterDatabase.getConnection().createStatement()
                .execute("delete from "+adapterDatabase.getTableName()+" where master_id='"+masterId+"'");
    }

    @Override
    public boolean contains(Integer masterId, Integer slaveId) throws SQLException{
        List<Integer> slaveIdList = get(masterId);
        if(slaveIdList.contains(slaveId)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getTableName() {
        return adapterDatabase.getTableName();
    }

    public AdapterDatabase getAdapterDatabase() {
        return adapterDatabase;
    }

    public void setAdapterDatabase(AdapterDatabase adapterDatabase) {
        this.adapterDatabase = adapterDatabase;
    }
}
