package org.demo.codegen.meta;

import java.util.ArrayList;
import java.util.List;

public class TableMeta {
    private String tableName;
    private String className;
    private List<ColumnMeta> columns = new ArrayList<ColumnMeta>();
    private String comment; // 表注释

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ColumnMeta> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMeta> columns) {
        this.columns = columns;
    }

    public void addColumn(ColumnMeta c) {
        this.columns.add(c);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
