package com.scau.myframework.orm.entity;

import java.util.List;
import java.util.Map;


/**
 * 存储表结构的信息
 * @author lipan
 *
 */
public class TableInfo {
	/**
	 * 表名
	 */
	private String tableName;
	
	/**
	 * 所有字段的信息
	 */
	private Map<String,ColumnInfo> columns;
	
	/**
	 * 唯一主键(目前我们只能处理表中有且只有一个主键的情况)
	 */
	private ColumnInfo onlyPrimaryKey;
	
	
	/**
	 * 如果联合主键，则在这里存储
	 */
	private List<ColumnInfo> primaryKeys;
	
	
	public List<ColumnInfo> getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(List<ColumnInfo> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, ColumnInfo> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, ColumnInfo> columns) {
		this.columns = columns;
	}

	public ColumnInfo getOnlyPrimaryKey() {
		return onlyPrimaryKey;
	}

	public void setOnlyPrimaryKey(ColumnInfo onlyPrimaryKey) {
		this.onlyPrimaryKey = onlyPrimaryKey;
	}

	public TableInfo(String tableName, Map<String, ColumnInfo> columns,
					 ColumnInfo onlyPrimaryKey) {
		super();
		this.tableName = tableName;
		this.columns = columns;
		this.onlyPrimaryKey = onlyPrimaryKey;
	}
	
	public TableInfo() {
	}

	public TableInfo(String tableName, List<ColumnInfo> primaryKeys, Map<String, ColumnInfo> columns
			) {
		super();
		this.tableName = tableName;
		this.columns = columns;
		this.primaryKeys = primaryKeys;
	}
}
