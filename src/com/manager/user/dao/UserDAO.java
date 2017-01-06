package com.manager.user.dao;

import java.util.List;
import java.util.Map;

public interface UserDAO {
    //-------------------------- Any -------------------------------------------
    //queryAnyList
    public List queryAnyList(Map<String, Object> paramMap) throws Exception;

    //queryAny
    public Map queryAny(Map<String, Object> paramMap) throws Exception;

    //addAny
    public Long addAny(Map<String, Object> paramMap) throws Exception;

    //updateAny
    public Integer updateAny(Map<String, Object> map) throws Exception;

    //delAny
    public Integer delAny(Map<String, Object> map) throws Exception;

}
