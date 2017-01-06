package com.manager.user.bmo.impl;

import com.manager.user.bmo.UserBMO;
import com.manager.user.dao.UserDAO;

import java.util.List;
import java.util.Map;

public class UserBMOImpl implements UserBMO {

	private UserDAO userDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

    //-------------------------- Any -------------------------------------------
    //queryAnyList
    public List queryAnyList(Map<String, Object> paramMap) throws Exception {
        return this.userDAO.queryAnyList(paramMap);
    }
    //queryAny
    public Map queryAny(Map<String, Object> paramMap) throws Exception {
        return (Map)this.userDAO.queryAny(paramMap);
    }
    //addAny
    public Long addAny(Map<String,Object> paramMap) throws Exception{
        return this.userDAO.addAny(paramMap);
    }
    //updateAny
    public Integer updateAny(Map<String, Object> map) throws Exception{
        return this.userDAO.updateAny(map);
    }
    //delAny
    public Integer delAny(Map<String, Object> map) throws Exception{
        return this.userDAO.delAny(map);
    }

}
