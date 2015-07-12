package com.m1905.mobile.dao;

import java.util.ArrayList;
import java.util.List;

public class EColumn extends Entity {

	/**
	 * 栏目
	 */
	private static final long serialVersionUID = 1L;
	private List<Column> columnList;

	public List<Column> getColumnList() {
		return columnList == null ? columnList = new ArrayList<Column>()
				: columnList;
	}

	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}

}
