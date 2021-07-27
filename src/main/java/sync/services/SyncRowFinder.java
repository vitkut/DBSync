package sync.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.dao.AdapterDao;
import sync.models.Table;
import sync.models.primary.TableException;

import java.sql.SQLException;

public class SyncRowFinder {

    private static final Logger logger = LoggerFactory.getLogger(SyncRowFinder.class);
    private AdapterDao adapterDao;

    public SyncRowFinder(AdapterDao adapterDao) {
        this.adapterDao = adapterDao;
    }

    public void findSyncRows(Table masterTable, Table slaveTable) throws TableException, SQLException {
        try {
            if(!masterTable.getRows().get(0).equals(slaveTable.getRows().get(0))){
                throw new TableException("Columns in the tables are not the same");
            }
            Integer masterId;
            Integer slaveId;
            for(int i = 1; i < masterTable.getRows().size(); i++){
                masterId = masterTable.getRows().get(i).getId();
                for(int j = 1; j < slaveTable.getRows().size(); j++){
                    slaveId = slaveTable.getRows().get(j).getId();
                    if(masterTable.getRows().get(i).equals(slaveTable.getRows().get(j))){
                        if(!adapterDao.contains(masterId, slaveId)){
                            adapterDao.put(masterId, slaveId);
                        }
                    }
                }
            }
        } catch (TableException | SQLException ex){
            logger.error("findSyncRows: "+ex.getMessage());
            throw ex;
        }
    }
}
