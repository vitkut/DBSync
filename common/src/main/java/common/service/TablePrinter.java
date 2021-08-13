package common.service;

import common.entity.Column;
import common.entity.Row;
import common.entity.Table;

import java.util.ArrayList;
import java.util.List;

public class TablePrinter {

    public static StringBuilder tableToStrBuilder(Table table){
        StringBuilder str = new StringBuilder();
        List<Column> columns = table.getColumns();
        str.append("Table name: ").append(table.getName()).append("\n");
        if(table.getColumns().isEmpty()){
            return str;
        }
        List<Integer> columnsWidths = new ArrayList<>();
        int maxWidth;
        for(Column column:columns){
            maxWidth = column.getName().length();
            if (maxWidth < column.getType().length()){
                maxWidth = column.getType().length();
            }
            for(String value:column.getValues()){
                if(maxWidth < value.length()){
                    maxWidth = value.length();
                }
            }
            columnsWidths.add(maxWidth);
        }
        StringBuilder separator = new StringBuilder();
        for (Integer width:columnsWidths){
            for(int i = 0; i < width; i++){
                separator.append("-");
            }
            separator.append("+");
        }
        List<String> formats = new ArrayList<>();
        for(Integer width:columnsWidths){
            formats.add("%-"+width+"s|");
        }

        //header
        str.append(separator).append("\n");
        for(int i = 0; i < columns.size(); i++){
            str.append(String.format(formats.get(i), columns.get(i).getName()));
        }
        str.append("\n");
        for(int i = 0; i < columns.size(); i++){
            str.append(String.format(formats.get(i), columns.get(i).getType()));
        }
        str.append("\n");
        str.append(separator).append("\n");

        //body
        for(int i = 0; i < table.size(); i++){
            Row row = table.getRow(i);
            for(int j = 0; j < columns.size(); j++){
                str.append(String.format(formats.get(j), row.getValues().get(j)));
            }
            str.append("\n");
        }
        str.append(separator).append("\n");
        return str;
    }

    public static StringBuilder columnToStrBuilder(Column column){
        Table table = new Table("");
        table.addColumn(column);
        return tableToStrBuilder(table);
    }
}
