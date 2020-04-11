package com.zking.dao;

import org.apache.commons.dbutils.QueryRunner;

public class QueryRunnerHelper {
	
	private QueryRunner runner = null;

	public QueryRunnerHelper() {
		DBPool dbPool = DBPool.getInstance();
		if (dbPool.getDataSource() != null) {
			runner = new QueryRunner(dbPool.getDataSource());
		} else {
			runner = new QueryRunner();
		}
	}

	public QueryRunner getRunner() {
		return runner;
	}
	
}
