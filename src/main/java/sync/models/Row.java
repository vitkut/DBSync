package sync.models;

import java.util.List;
import java.util.Objects;

public class Row {

    private Integer id;
    private List<String> values;

    public Row(Integer id, List<String> values) {
        this.id = id;
        this.values = values;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Row)) return false;
        Row row = (Row) o;
        return Objects.equals(id, row.id) &&
                Objects.equals(values, row.values);
    }

    @Override
    public String toString() {
        return "Row{" +
                "id=" + id +
                ", values=" + values +
                '}';
    }
}
