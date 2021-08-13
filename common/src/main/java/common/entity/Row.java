package common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Row {

    private final Integer id;
    private final List<String> values;

    public Row(Integer id, List<String> values) {
        this.id = id;
        this.values = values;
    }

    public Integer getId() {
        return id;
    }

    public List<String> getValues() {
        return new ArrayList<>(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return Objects.equals(id, row.id) &&
                Objects.equals(values, row.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, values);
    }

    @Override
    public String toString() {
        return "Row{" +
                "id=" + id +
                ", values=" + values +
                '}';
    }
}
