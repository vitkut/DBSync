package common.entity;

import common.service.TablePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Table {

    private final String name;
    private final List<Column> columns;

    public Table(String name) {
        this.name = name;
        this.columns = new ArrayList<>();
    }

    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public Row getRow(Integer index){
        if(index < size() && index >= 0){
            List<String> values = new ArrayList<>();
            for(Column column:columns){
                values.add(column.getValues().get(index));
            }
            return new Row(index, values);
        } else {
            return new Row(index, new ArrayList<>());
        }
    }

    public List<Column> getColumns() {
        return new ArrayList<>(columns);
    }

    public void addRow(Row row){
        if(isRowValid(row)){
            for(int i = 0; i < columns.size(); i++){
                columns.get(i).add(row.getValues().get(i));
            }
        }
    }

    private boolean isRowValid(Row row){
        if(row.getValues().size() != columns.size()){
            return false;
        }
        return true;
    }

    public void addColumn(Column column){
        columns.add(column);
    }

    public Integer size(){
        if(columns.isEmpty()){
            return 0;
        }
        return columns.get(0).getValues().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(name, table.name) &&
                Objects.equals(columns, table.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, columns);
    }

    @Override
    public String toString() {
        return TablePrinter.tableToStrBuilder(this).toString();
    }
}
