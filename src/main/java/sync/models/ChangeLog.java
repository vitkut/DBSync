package sync.models;

import sync.models.primary.ChangeMethod;

public class ChangeLog {

    private ChangeMethod changeMethod;
    private Row columns;
    private Row from;
    private Row to;

    public ChangeLog(ChangeMethod changeMethod, Row columns, Row from, Row to) {
        this.changeMethod = changeMethod;
        this.columns = columns;
        this.from = from;
        this.to = to;
    }


    public ChangeMethod getChangeMethod() {
        return changeMethod;
    }

    public void setChangeMethod(ChangeMethod changeMethod) {
        this.changeMethod = changeMethod;
    }

    public Row getColumns() {
        return columns;
    }

    public void setColumns(Row columns) {
        this.columns = columns;
    }

    public Row getFrom() {
        return from;
    }

    public void setFrom(Row from) {
        this.from = from;
    }

    public Row getTo() {
        return to;
    }

    public void setTo(Row to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "ChangeLog{" +
                "changeMethod=" + changeMethod +
                ", columns=" + columns +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
