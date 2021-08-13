package common.entity;

import common.service.TablePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Column {

    private final String name;
    private final String type;
    private final List<String> values;

    public Column(String name, String type) {
        this.name = name;
        this.type = type;
        this.values = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<String> getValues() {
        return new ArrayList<>(values);
    }

    public void add(String value){
        values.add(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equals(name, column.name) &&
                Objects.equals(type, column.type) &&
                Objects.equals(values, column.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, values);
    }

    @Override
    public String toString() {
        return TablePrinter.columnToStrBuilder(this).toString();
    }
}
