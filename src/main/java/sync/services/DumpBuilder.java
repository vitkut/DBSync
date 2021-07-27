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
        } catch (IOException ex){
            logger.error("writeDump: "+ex.getMessage());
            throw ex;
        }
    }

    public Table readDump() throws IOException, ClassNotFoundException{
        try {
            if(!dump.exists()){
                throw new FileNotFoundException("Can not find dump in path ["+dump.getPath()+"]");
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(dump));
            Table table = (Table) objectInputStream.readObject();
            objectInputStream.close();
            return table;
        } catch (IOException | ClassNotFoundException ex){
            logger.error("readDump: "+ex.getMessage());
            throw ex;
        }
    }

    public boolean isExists(){
        return this.dump.exists();
    }
}
