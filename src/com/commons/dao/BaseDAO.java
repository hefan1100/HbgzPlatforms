package com.commons.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;


public class BaseDAO extends IBatisDaoSupport {

	/**
	 * 分页方法
	 * @param <T>
	 * @param statementName
	 * @param page
	 * @return
	 */
	public <T> List<T> queryForPageList(String statementName, Page page) throws DataAccessException{
		int index = (page.getCurPage() - 1) * page.getPageSize();
		int size = page.getPageSize();
		return queryForPageList(statementName, index, size);
	}
	public <T> List<T> queryForPageList(String statementName, int index, int size) throws DataAccessException{

		return getSqlMapClientTemplate().queryForList(statementName, index, size);
	}
	/**
	 * 带参数的分页方法
	 * @param <T>
	 * @param statementName
	 * @param param
	 * @param page
	 * @return
	 */
	public <T> List<T> queryForPageList(String statementName, Object param, Page page) throws DataAccessException{
		int index = (page.getCurPage() - 1) * page.getPageSize();
		int size = page.getPageSize();
		return this.queryForPageList(statementName, param, index, size);
	}
	
	public <T> List<T> queryForPageList(String statementName, Object param, int index, int size) throws DataAccessException{
		return getSqlMapClientTemplate().queryForList(statementName, param, index, size);
	}

	@Override
	public void init() {
		super.init();
	}
	
}
