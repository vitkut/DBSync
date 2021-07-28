package sync.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Table implements Serializable {

    private List<Row> rows; //first Row - columns

    public Table() {
        this.rows = new ArrayList<>();
    }

    public Table(List<Row> rows) {
        this.rows = rows;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(rows, table.rows);
    }

    @Override
    public String toString() {
        return "Table{" +
                "rows=" + rows +
                '}';
    }
}
