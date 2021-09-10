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
            logger.debug("findSyncRows("+masterTable+", "+slaveTable+")");
            Integer masterId;
            Integer slaveId;
            for(int i = 1; i < masterTable.getRows().size(); i++){
                masterId = masterTable.getRows().get(i).getId();
                for(int j = 1; j < slaveTable.getRows().size(); j++){
                    slaveId = slaveTable.getRows().get(j).getId();
                    if(masterTable.getRows().get(i).getValues().equals(slaveTable.getRows().get(j).getValues())){
                        if(!adapterDao.contains(masterId, slaveId)){
                            adapterDao.put(masterId, slaveId);
                            logger.debug("findSyncRows -- adapterDao added masterID="+masterId+", slaveID="+slaveId);
                        }
                    }
                }
            }
            logger.debug("findSyncRows done");
        } catch (Exception ex){
            logger.error("findSyncRows: "+ex.getMessage());
            throw ex;
        }
    }
}
