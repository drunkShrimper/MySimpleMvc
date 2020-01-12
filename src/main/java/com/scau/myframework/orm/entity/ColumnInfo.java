package com.scau.myframework.orm.entity;

/**
 * 封装表中一个字段的信息
 *
 * @author lipan
 */
public class ColumnInfo {
    /**
     * 字段名称
     */
    private String columnName;

    /**
     * 字段的数据类型
     */
    private String dataType;

    /**
     * 字段的键类型(0：普通键，1：主键 2：外键)
     */
    private int keyType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public ColumnInfo(String columnName, String dataType, int keyType) {
        super();
        this.columnName = columnName;
        this.dataType = dataType;
        this.keyType = keyType;
    }

    public ColumnInfo() {
    }
}
