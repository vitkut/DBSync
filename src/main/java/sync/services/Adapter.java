package sync.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.dao.AdapterDao;
import sync.models.ChangeLog;
import sync.models.Table;
import sync.models.primary.ChangeMethod;
import sync.models.Row;
import sync.services.utils.SQLBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Adapter {

    private AdapterDao adapterDao;
    private static final Logger logger = LoggerFactory.getLogger(Adapter.class);

    public Adapter(AdapterDao adapterDao) {
        this.adapterDao = adapterDao;
    }

    public List<ChangeLog> adapt(List<ChangeLog> masterChangesLogs) {
        logger.debug("adapt("+masterChangesLogs+")");
        List<ChangeLog> adaptedChangesLogs = new ArrayList<>();
        try{
            for(ChangeLog c:masterChangesLogs){
                Integer masterId;
                if(c.getChangeMethod().equals(ChangeMethod.ADD)){
                    adaptedChangesLogs.add(c);
                }
                if(c.getChangeMethod().equals(ChangeMethod.DEL)){
                    masterId = c.getFrom().getId();
                    List<Integer> slaveIdList = adapterDao.get(c.getFrom().getId());
                    for(Integer id:slaveIdList){
                        adaptedChangesLogs.add(
                                new ChangeLog(ChangeMethod.DEL, c.getColumns(), new Row(id, c.getFrom().getValues()), null));
                    }
                    adapterDao.removeByMaster(masterId);
                }
                if(c.getChangeMethod().equals(ChangeMethod.UPD)){
                    List<Integer> slaveIdList = adapterDao.get(c.getFrom().getId());
                    for(Integer id:slaveIdList){
                        adaptedChangesLogs.add(
                                new ChangeLog(ChangeMethod.UPD, c.getColumns(),
                                        new Row(id, c.getFrom().getValues()), new Row(id, c.getTo().getValues())));
                    }
                }
            }
        } catch (Exception ex){
            logger.error("adapt: "+ex.getMessage());
        }
        logger.debug("adapt -> "+adaptedChangesLogs);
        return adaptedChangesLogs;
    }

    public List<Integer> getAllMastersId() throws SQLException{
        try {
            logger.debug("getAllMastersId()");
            Table table = adapterDao.getAll();
            List<Integer> masterIdList = new ArrayList<>();
            int columnNum = 0;
            if(table.getRows().get(0).getValues().get(1).equals("master_id")){
                columnNum = 1;
            }
            for(int i = 1; i < table.getRows().size(); i++){
                masterIdList.add(Integer.parseInt(table.getRows().get(i).getValues().get(columnNum)));
            }
            logger.debug("getAllMastersId -> "+masterIdList);
            return masterIdList;
        } catch (Exception ex){
            logger.error("getAllMastersId: "+ex.getMessage());
            throw ex;
        }
    }

    public AdapterDao getAdapterDao() {
        return adapterDao;
    }

    public void setAdapterDao(AdapterDao adapterDao) {
        this.adapterDao = adapterDao;
    }
}
