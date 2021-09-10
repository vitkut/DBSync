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
    private List<Row> columns;
    private List<Row> convertColumns;
    private static final Logger logger = LoggerFactory.getLogger(Adapter.class);

    public Adapter(AdapterDao adapterDao, List<Row> columns, List<Row> convertColumns) {
        this.adapterDao = adapterDao;
        this.columns = columns;
        this.convertColumns = convertColumns;
    }

    public List<ChangeLog> adapt(List<ChangeLog> masterChangesLogs) {
        logger.debug("adapt("+masterChangesLogs+")");
        List<ChangeLog> adaptedChangesLogs = new ArrayList<>();
        try{
            for(ChangeLog c:masterChangesLogs){
                Integer masterId;
                if(c.getChangeMethod().equals(ChangeMethod.ADD)){
                    adaptedChangesLogs.add(new ChangeLog(ChangeMethod.ADD,
                            adaptColumns(c.getColumns()),
                            convertRow(c.getColumns(), c.getFrom()),
                            convertRow(c.getColumns(), c.getTo())));
                }
                if(c.getChangeMethod().equals(ChangeMethod.DEL)){
                    masterId = c.getFrom().getId();
                    List<Integer> slaveIdList = adapterDao.get(c.getFrom().getId());
                    for(Integer id:slaveIdList){
                        adaptedChangesLogs.add(
                                new ChangeLog(ChangeMethod.DEL,
                                        adaptColumns(c.getColumns()),
                                        new Row(id, convertRow(c.getColumns(), c.getFrom()).getValues()),
                                        null));
                    }
                    adapterDao.removeByMaster(masterId);
                }
                if(c.getChangeMethod().equals(ChangeMethod.UPD)){
                    List<Integer> slaveIdList = adapterDao.get(c.getFrom().getId());
                    for(Integer id:slaveIdList){
                        adaptedChangesLogs.add(
                                new ChangeLog(ChangeMethod.UPD,
                                        adaptColumns(c.getColumns()),
                                        new Row(id, convertRow(c.getColumns(), c.getFrom()).getValues()),
                                        new Row(id, convertRow(c.getColumns(), c.getTo()).getValues())));
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

    private Row adaptColumns(Row masterColumns){
        List<String> adaptedColumns = new ArrayList<>();
        for(String c:masterColumns.getValues()){
            int num = columns.get(0).getValues().indexOf(c);
            adaptedColumns.add(columns.get(1).getValues().get(num));
        }
        return new Row(masterColumns.getId(), adaptedColumns);
    }

    public Row convertRow(Row columns, Row masterRow){
        if(masterRow == null){
            return null;
        }
        List<String> values = new ArrayList<>();
        for (int i = 0; i < columns.getValues().size(); i++){
            String newValue;
            if(convertColumns.get(0).getValues().contains(columns.getValues().get(i))){
                int num = convertColumns.get(0).getValues().indexOf(columns.getValues().get(i));
                String value = masterRow.getValues().get(i);
                String method = convertColumns.get(1).getValues().get(num);
                logger.debug("convert: column=["+columns.getValues().get(i)+"], value=["+value+"], method=["+method+"]");
                newValue = ValuesConverter.convert(value, method);
            } else {
                newValue = masterRow.getValues().get(i);
            }
            values.add(newValue);
        }
        return new Row(masterRow.getId(), values);
    }

    public AdapterDao getAdapterDao() {
        return adapterDao;
    }

    public void setAdapterDao(AdapterDao adapterDao) {
        this.adapterDao = adapterDao;
    }
}
