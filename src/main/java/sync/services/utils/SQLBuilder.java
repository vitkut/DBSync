package sync.services.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sync.models.Row;

public class SQLBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SQLBuilder.class);

    public static String getSelectSQL(Row columns, String tableName){
        logger.debug("getSelectSQL("+columns+", "+tableName+")");
        StringBuilder sql = new StringBuilder();
        sql.append("select ID, ");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i)).append(", ");
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1));
        sql.append(" from ").append(tableName);
        logger.debug("getSelectSQL -> "+sql.toString());
        return sql.toString();
    }

    public static String getUpdateSQL(String tableName, Row columns, Row from, Row to){
        logger.debug("getUpdateSQL("+tableName+", "+columns+", "+to+")");
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(tableName).append(" set ");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i)).append("=");
            if(to.getValues().get(i) == null){
                sql.append("NULL, ");
            } else {
                sql.append("'").append(to.getValues().get(i)).append("', ");
            }
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1)).append("=");
        if(to.getValues().get(columns.getValues().size()-1) == null){
            sql.append(to.getValues().get(columns.getValues().size()-1));
        } else {
            sql.append("'").append(to.getValues().get(columns.getValues().size()-1)).append("'");
        }
        sql.append(" where ");
        sql.append("ID='").append(from.getId()).append("' AND ");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i));
            if(from.getValues().get(i) == null){
                sql.append(" IS NULL AND ");
            } else {
                sql.append("='").append(from.getValues().get(i)).append("' AND ");
            }
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1));
        if(from.getValues().get(columns.getValues().size()-1) == null){
            sql.append(" IS NULL");
        } else {
            sql.append("='").append(from.getValues().get(columns.getValues().size()-1)).append("'");
        }
        logger.debug("getUpdateSQL -> "+sql.toString());
        return sql.toString();
    }

    public static String getDeleteSQL(String tableName, Row columns, Row from){
        logger.debug("getDeleteSQL("+tableName+", "+columns+", "+from+")");
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(tableName).append(" where ");
        sql.append("ID='").append(from.getId()).append("' AND ");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i));
            if(from.getValues().get(i) == null){
                sql.append(" IS NULL AND ");
            } else {
                sql.append("='").append(from.getValues().get(i)).append("' AND ");
            }
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1));
        if(from.getValues().get(columns.getValues().size()-1) == null){
            sql.append(" IS NULL");
        } else {
            sql.append("='").append(from.getValues().get(columns.getValues().size()-1)).append("'");
        }
        logger.debug("getDeleteSQL -> "+sql.toString());
        return sql.toString();
    }

    public static String getAddSQL(String tableName, Row columns, Row to){
        logger.debug("getAddSQL("+tableName+", "+columns+", "+to+")");
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tableName).append("(");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i)).append(", ");
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1)).append(") values (");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            if(to.getValues().get(i) == null){
                sql.append("NULL").append(", ");
            } else {
                sql.append("'").append(to.getValues().get(i)).append("', ");
            }
        }
        if(to.getValues().get(columns.getValues().size()-1) == null){
            sql.append(to.getValues().get(columns.getValues().size()-1)).append(")");
        } else {
            sql.append("'").append(to.getValues().get(columns.getValues().size()-1)).append("')");
        }
        logger.debug("getAddSQL -> "+sql.toString());
        return sql.toString();
    }

    public static String getId(String tableName, Row columns, Row from){
        logger.debug("getId("+tableName+", "+columns+", "+from+")");
        StringBuilder sql = new StringBuilder();
        sql.append("select ID, ");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i)).append(", ");
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1));
        sql.append(" from ").append(tableName).append(" where ");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i));
            if(from.getValues().get(i) == null){
                sql.append(" IS NULL AND ");
            } else {
                sql.append("='").append(from.getValues().get(i)).append("' AND ");
            }
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1));
        if(from.getValues().get(columns.getValues().size()-1) == null){
            sql.append(" IS NULL");
        } else {
            sql.append("='").append(from.getValues().get(columns.getValues().size()-1)).append("'");
        }
        logger.debug("getId -> "+sql.toString());
        return sql.toString();
    }
}
