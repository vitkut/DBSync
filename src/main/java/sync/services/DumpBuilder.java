package sync.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.models.Table;

import java.io.*;

public class DumpBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DumpBuilder.class);
    private File dump;

    public DumpBuilder(String dumpPath) {
        this.dump = new File(dumpPath);
    }

    public void writeDump(Table table) throws IOException{
        try {
            logger.debug("writeDump("+table+")");
            if(dump.exists()){
                if(!dump.delete()){
                    throw new IOException("Can not delete previous dump file in path ["+dump.getPath()+"]");
                }
            }
            if (!dump.createNewFile()){
                throw new IOException("Can not create new dump file in path ["+dump.getPath()+"]");
            }
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dump));
            objectOutputStream.writeObject(table);
            objectOutputStream.close();
            logger.debug("writeDump done");
        } catch (Exception ex){
            logger.error("writeDump: "+ex);
            throw ex;
        }
    }

    public Table readDump() throws IOException, ClassNotFoundException{
        try {
            logger.debug("readDump()");
            if(!dump.exists()){
                throw new FileNotFoundException("Can not find dump in path ["+dump.getPath()+"]");
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(dump));
            Table table = (Table) objectInputStream.readObject();
            objectInputStream.close();
            logger.debug("readDump -> "+table);
            return table;
        } catch (Exception ex){
            logger.error("readDump: "+ex.getMessage());
            throw ex;
        }
    }

    public boolean isExists(){
        logger.debug("isExists -> "+this.dump.exists());
        return this.dump.exists();
    }
}
