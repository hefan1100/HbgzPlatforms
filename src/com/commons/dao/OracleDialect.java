package com.commons.dao;


public class OracleDialect {

	protected static final String SQL_END_DELIMITER = ";";

	public static String getLimitString(String sql, boolean hasOffset) {
		StringBuilder pagingSelect = new StringBuilder( sql.length()+100 );
		if (hasOffset) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		}
		else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (hasOffset) {
			pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
		}
		else {
			pagingSelect.append(" ) where rownum <= ?");
		}
		return pagingSelect.toString();
	}
	/**
	 * Oracle定制的分页模板语句
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static String getLimitString(String sql, int offset, int limit) {
		if (offset == 1) {
			offset = 0;
		}
		StringBuilder pageStr = new StringBuilder();
		pageStr.append("SELECT /*+ FIRST_ROWS */ * FROM (SELECT ROW_.*, ROWNUM ROWNUM_ FROM (");
		pageStr.append(OracleDialect.trim(sql));
		pageStr.append(") ROW_ WHERE ROWNUM <= ");
		pageStr.append(limit + offset);
		pageStr.append(") WHERE ROWNUM_ >");
		pageStr.append(offset);
		return pageStr.toString();
	}

	/**    
	 * 去掉SQL后分号
     * @param sql
	 * @return    
	 */
	private static String trim(String sql) {
		sql = sql.trim();
		if (sql.endsWith(SQL_END_DELIMITER)) {
			sql = sql.substring(0, sql.length() - 1 - SQL_END_DELIMITER.length());
		}
		return sql;
	}
}
