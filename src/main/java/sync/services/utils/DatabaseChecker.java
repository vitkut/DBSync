package sync.services.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.models.*;
import sync.models.primary.ChangeMethod;
import sync.models.primary.TableException;

import java.util.ArrayList;
import java.util.List;

public class DatabaseChecker {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseChecker.class);

    public static List<ChangeLog> getChangeLogs(Table oldDump, Table newDump) throws TableException {
        try {
            logger.debug("getChangeLogs("+oldDump+", "+newDump+")");
            if(!oldDump.getRows().get(0).equals(newDump.getRows().get(0))){
                throw new TableException("Columns in the dumps are not the same");
            }
            List<ChangeLog> changeLogs = new ArrayList<>();
            Row columns = newDump.getRows().get(0);
            int iterOld = 1;
            int iterNew = 1;
            while (iterOld < oldDump.getRows().size() && iterNew < newDump.getRows().size()){
                Row oldRow = oldDump.getRows().get(iterOld);
                Row newRow = newDump.getRows().get(iterNew);
                if(oldRow.getId() > newRow.getId()){
                    //add
                    changeLogs.add(new ChangeLog(ChangeMethod.ADD, columns, null, newRow));
                    iterNew++;
                }
                if(oldRow.getId() < newRow.getId()){
                    //del
                    changeLogs.add(new ChangeLog(ChangeMethod.DEL, columns, oldRow, null));
                    iterOld++;
                }
                if(oldRow.getId().equals(newRow.getId())){
                    //upd or not
                    if(!oldRow.getValues().equals(newRow.getValues())){
                        changeLogs.add(new ChangeLog(ChangeMethod.UPD, columns, oldRow, newRow));
                    }
                    iterNew++;
                    iterOld++;
                }
            }
            if(iterOld < oldDump.getRows().size()){
                //del all last
                for(int i = iterOld; i < oldDump.getRows().size(); i++){
                    Row oldRow = oldDump.getRows().get(i);
                    changeLogs.add(new ChangeLog(ChangeMethod.DEL, columns, oldRow, null));
                }
            }
            if(iterNew < newDump.getRows().size()){
                //add all last
                for(int i = iterNew; i < newDump.getRows().size(); i++){
                    Row newRow = newDump.getRows().get(i);
                    changeLogs.add(new ChangeLog(ChangeMethod.ADD, columns, null, newRow));
                }
            }
            logger.debug("getChangeLogs -> "+changeLogs);
            return changeLogs;
        } catch (Exception ex){
            logger.error("getChangeLogs: "+ex.getMessage());
            throw ex;
        }
    }
}
