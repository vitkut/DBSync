package sync.services.utils;

import sync.models.Row;

public class SQLBuilder {

    public static String getSelectSQL(Row columns, String tableName){
        StringBuilder sql = new StringBuilder();
        sql.append("select ID, ");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i)).append(", ");
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1));
        sql.append(" from ").append(tableName);
        return sql.toString();
    }

    public static String getUpdateSQL(String tableName, Row columns, Row from, Row to){
        StringBuilder sql = new StringBuilder();
        sql.append("update table ").append(tableName).append(" set ");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i)).append("='");
            sql.append(to.getValues().get(i)).append("', ");
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1)).append("='");
        sql.append(to.getValues().get(columns.getValues().size()-1)).append("'");
        sql.append(" where ");
        sql.append("ID='").append(from.getId()).append("' AND ");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i)).append("='");
            sql.append(from.getValues().get(i)).append("' AND ");
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1)).append("='");
        sql.append(from.getValues().get(columns.getValues().size()-1)).append("'");
        return sql.toString();
    }

    public static String getDeleteSQL(String tableName, Row columns, Row from){
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(tableName).append(" where ");
        sql.append("ID='").append(from.getId()).append("' AND ");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i)).append("='");
            sql.append(from.getValues().get(i)).append("' AND ");
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1)).append("='");
        sql.append(from.getValues().get(columns.getValues().size()-1)).append("'");
        return sql.toString();
    }

    public static String getAddSQL(String tableName, Row columns, Row to){
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tableName).append("(");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i)).append(", ");
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1)).append(") values (");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i)).append("='");
            sql.append(to.getValues().get(i)).append("', ");
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1)).append("='");
        sql.append(to.getValues().get(columns.getValues().size()-1)).append("')");
        return sql.toString();
    }

    public static String getId(String tableName, Row columns, Row from){
        StringBuilder sql = new StringBuilder();
        sql.append("select ID from ").append(tableName).append(" where ");
        for(int i = 0; i < columns.getValues().size()-1; i++){
            sql.append(columns.getValues().get(i)).append("='");
            sql.append(from.getValues().get(i)).append("' AND ");
        }
        sql.append(columns.getValues().get(columns.getValues().size()-1)).append("='");
        sql.append(from.getValues().get(columns.getValues().size()-1)).append("'");
        return sql.toString();
    }
}
