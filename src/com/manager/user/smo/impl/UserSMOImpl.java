package com.manager.user.smo.impl;

import com.manager.user.bmo.UserBMO;
import com.manager.user.smo.UserSMO;

import java.util.List;
import java.util.Map;

public class UserSMOImpl implements UserSMO {
    private UserBMO userBMO;

    public UserBMO getUserBMO() {
        return userBMO;
    }

    public void setUserBMO(UserBMO userBMO) {
        this.userBMO = userBMO;
    }

    //-------------------------- Any -------------------------------------------
    //queryAnyList
    public List queryAnyList(Map<String, Object> paramMap) throws Exception {
        return this.userBMO.queryAnyList(paramMap);
    }
    //queryAny
    public Map queryAny(Map<String, Object> paramMap) throws Exception {
        return (Map)this.userBMO.queryAny(paramMap);
    }
    //addAny
    public Long addAny(Map<String,Object> paramMap) throws Exception{
        return this.userBMO.addAny(paramMap);
    }
    //updateAny
    public Integer updateAny(Map<String, Object> map) throws Exception{
        return this.userBMO.updateAny(map);
    }
    //delAny
    public Integer delAny(Map<String, Object> map) throws Exception{
        return this.userBMO.delAny(map);
    }
}
