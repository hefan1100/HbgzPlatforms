package com.manager.user.dao.impl;

import com.manager.user.dao.UserDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO {

    private SqlMapClient sqlMapClient;

    public SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    //-------------------------- Any -------------------------------------------
    //queryAnyList
    public List queryAnyList(Map<String, Object> paramMap) throws Exception {
        return (List) this.sqlMapClient.queryForList("user.queryAnyList", paramMap);
    }
    //queryAny
    public Map queryAny(Map<String, Object> paramMap) throws Exception {
        return (Map) this.sqlMapClient.queryForObject("user.queryAny", paramMap);
    }
    //addAny
    public Long addAny(Map<String, Object> paramMap) throws Exception {
        return (Long) this.sqlMapClient.insert("user.addAny", paramMap);
    }
    //updateAny
    public Integer updateAny(Map<String, Object> map) throws Exception {
        return this.sqlMapClient.update("user.updateAny", map);
    }
    //delAny
    public Integer delAny(Map<String, Object> map) throws Exception {
        return this.sqlMapClient.update("user.delAny", map);
    }
}
