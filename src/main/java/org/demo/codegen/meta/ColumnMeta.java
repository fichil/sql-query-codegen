package org.demo.codegen.meta;

public class ColumnMeta {
    private String columnName;   // 原始列名：CREATE_TIME
    private String mysqlType;    // 原始类型：datetime / varchar(32) ...
    private String javaName;     // createTime
    private String javaType;     // Date / String / Long / Double
    private boolean timeType;    // date/datetime/timestamp
    private String comment;      // 列注释：创建时间

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getMysqlType() {
        return mysqlType;
    }

    public void setMysqlType(String mysqlType) {
        this.mysqlType = mysqlType;
    }

    public String getJavaName() {
        return javaName;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public boolean isTimeType() {
        return timeType;
    }

    public void setTimeType(boolean timeType) {
        this.timeType = timeType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
