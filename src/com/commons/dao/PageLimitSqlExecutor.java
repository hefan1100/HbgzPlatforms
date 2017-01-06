package com.commons.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.StatementScope;

/**
 * 重载ibatis的sqlExcutor的默认的分页实现，用oracle的物理分页
 *
 */
public class PageLimitSqlExecutor extends SqlExecutor {

	/**  
	 *  重写SqlExecutor.executeQuery方法 实现ORACLE的SQL物理分页
	 */
	@Override
	public void executeQuery(StatementScope request, Connection conn,      
            String sql, Object[] parameters, int skipResults, int maxResults,      
            RowHandlerCallback callback) throws SQLException {      
        if (isLimit(sql, skipResults, maxResults)) {// 有分页信息、可物理分页SQL
            sql = OracleDialect.getLimitString(sql, skipResults, maxResults);// 获得物理分页SQL
            skipResults = NO_SKIPPED_RESULTS;// 设置skipResults为SqlExecutor不分页
            maxResults = NO_MAXIMUM_RESULTS;//  设置maxResults为SqlExecutor不分页
        }      
        super.executeQuery(request, conn, sql, parameters, skipResults,      
                maxResults, callback);//  使用不分页机制调用SqlExecutor查询方法
    }      

	 /**    
     * 是否允许执行分页
     *     
     * @param sql    
     * @param skipResults    
     * @param maxResults    
     * @return    
     */     
    private boolean isLimit(String sql, int skipResults, int maxResults) {      
        return (skipResults != NO_SKIPPED_RESULTS || maxResults != NO_MAXIMUM_RESULTS)      
                && isSelect(sql);      
    }      
     
    /**    
     * 是否可物理分页SQL
     *     
     * @param sql    
     * @return    
     */     
    private boolean isSelect(String sql) {      
        if (sql.toLowerCase().indexOf("select") >= 0) {      
            return true;      
        }      
        return false;      
    }
}
