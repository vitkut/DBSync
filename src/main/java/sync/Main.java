package sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.config.AppConfig;
import sync.dao.*;
import sync.models.*;
import sync.models.primary.ChangeMethod;
import sync.services.Adapter;
import sync.services.DumpBuilder;
import sync.services.PropertiesReader;
import sync.services.SyncRowFinder;
import sync.services.utils.DatabaseChecker;
import sync.services.utils.MasterSlaveInitializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static Integer period;
    private static Timer timer;
    private static Adapter adapter;
    private static MasterDao masterDao;
    private static SlaveDao slaveDao;
    private static AdapterDao adapterDao;
    private static DumpBuilder dumpBuilder;
    private static PropertiesReader propertiesReader;
    private static SyncRowFinder syncRowFinder;
    private static String dumpPath;
    private static boolean paused;
    private static boolean error = false;

    public static void init(){
        try {
            logger.info("Init..");
            propertiesReader = new PropertiesReader();
            AppConfig appConfig = new AppConfig(propertiesReader);
            period = appConfig.getPeriod();
            dumpPath = appConfig.getDumpPath();
            MasterDatabase masterDatabase = appConfig.getMasterDatabase();
            SlaveDatabase slaveDatabase = appConfig.getSlaveDatabase();
            AdapterDatabase adapterDatabase = appConfig.getAdapterDatabase(masterDatabase);
            adapterDao = new AdapterDaoImpl(adapterDatabase);
            Row sameColumns = MasterSlaveInitializer.getSameColumns(masterDatabase, slaveDatabase);
            masterDao = new MasterDaoImpl(masterDatabase, sameColumns);
            slaveDao = new SlaveDaoImpl(slaveDatabase, sameColumns);
            adapter = new Adapter(adapterDao);
            dumpBuilder = new DumpBuilder(dumpPath);
            syncRowFinder = new SyncRowFinder(adapterDao);

            boolean needToCreateDump = false;
            if(dumpBuilder.isExists()){
                try {
                    Table dump = dumpBuilder.readDump();
                    if(dump == null){
                        throw new Exception();
                    }
                } catch (Exception ex){
                    needToCreateDump = true;
                }
            } else {
                needToCreateDump = true;
            }
            if(needToCreateDump){
                List<Integer> allMasterIdList = adapter.getAllMastersId();
                Table dump = masterDao.getAll();
                List<Row> removeList = new ArrayList<>();
                for(int i = 1; i < dump.getRows().size(); i++){
                    if(!allMasterIdList.contains(dump.getRows().get(i).getId())){
                        removeList.add(dump.getRows().get(i));
                    }
                }
                for (Row r:removeList){
                    dump.getRows().remove(r);
                }
                dumpBuilder.writeDump(dump);
            }
            syncRowFinder.findSyncRows(masterDao.getAll(), slaveDao.getAll());
        } catch (Exception ex){
            logger.error("init: "+ex.getMessage());
            error();
        }
    }

    public static void execute(){
        try {
            logger.info("Start checking..");
            Table newDump = masterDao.getAll();
            Table oldDump = dumpBuilder.readDump();
            List<ChangeLog> changeLogs = DatabaseChecker.getChangeLogs(oldDump, newDump);
            if(!changeLogs.isEmpty()){
                changeLogs = adapter.adapt(changeLogs);
                for(ChangeLog c:changeLogs){
                    if(c.getChangeMethod().equals(ChangeMethod.ADD)){
                        Integer slaveId = slaveDao.add(c);
                        Integer masterId = c.getTo().getId();
                        adapterDao.put(masterId, slaveId);
                    }
                    if(c.getChangeMethod().equals(ChangeMethod.DEL)){
                        slaveDao.delete(c);
                    }
                    if (c.getChangeMethod().equals(ChangeMethod.UPD)){
                        slaveDao.update(c);
                    }
                }
                dumpBuilder.writeDump(newDump);
            } else {
                logger.info("No changes");
            }

            logger.info("End checking..");
            logger.info("Next check in "+ LocalDateTime.now().plusSeconds(period/1000));
        } catch (Exception ex){
            logger.error("execute: "+ex.getMessage());
            error();
        }
    }

    public static void start(){
        timer = new Timer();
        paused = false;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!paused){
                    execute();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, period);
    }

    public static void pause(){
        paused = true;
    }

    public static void stop(){
        if(timer != null){
            timer.cancel();
        }
    }

    public static void error(){
        error = true;
        stop();
    }

    public static boolean isIsPaused() {
        return paused;
    }

    public static boolean isIsError() {
        return error;
    }
}
