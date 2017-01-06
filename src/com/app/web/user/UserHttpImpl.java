package com.app.web.user;

import com.commons.spring.SpringBeanInvoker;
import com.commons.util.JsonUtil;
import com.manager.user.smo.UserSMO;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHttpImpl {

    private static Logger log = Logger.getLogger(UserHttpImpl.class);
    private UserSMO userSMO;

    public UserSMO getUserSMO() {
        if (this.userSMO == null) {
            userSMO = (UserSMO) SpringBeanInvoker.getBean("userSMO");
        }
        return userSMO;
    }

    //queryAnyList
    public String queryAnyList(String jsonString) {
        try {
            Map<String, Object> param = (Map<String, Object>) JsonUtil.getObject(jsonString, Map.class);
            System.out.println("queryAnyListSQL:" + param.get("queryAnyListSQL"));
            List resultList = this.getUserSMO().queryAnyList(param);
            for(int i=0;i<resultList.size();i++)
            {
                HashMap map= (HashMap) resultList.get(i);
                if(map.containsKey("INSERTDATE"))
                {
                    java.sql.Date d= (Date) map.get("INSERTDATE");
                    map.remove("INSERTDATE");
                    System.out.println("Time:"+d.getTime());
                    java.util.Date s=new java.util.Date(d.getTime());
                    map.put("INSERTDATE",s);
                }
            }
            System.out.println("resultList.size():" + resultList.size());
            String returnJSON = JsonUtil.getJsonString(resultList);

            return returnJSON;
        } catch (Exception ex) {
            log.error("queryAnyListError", ex);
            return null;
        }
    }

    //queryAny
    public String queryAny(String jsonString) {
        try {
            Map<String, Object> param = (Map<String, Object>) JsonUtil.getObject(jsonString, Map.class);
            System.out.println("queryAnySQL:" + param.get("queryAnySQL"));
            Map<String, Object> result = this.getUserSMO().queryAny(param);
            String returnJSON = JsonUtil.getJsonString(result);
            return returnJSON;
        } catch (Exception ex) {
            log.error("queryAnyError", ex);
            return null;
        }
    }

    //addAny
    public String addAny(String jsonString) {
        try {
            boolean returnFlag = true;
            Map<String, Object> paramMap = (Map<String, Object>) JsonUtil.getObject(jsonString, Map.class);
            Long resultKey = this.getUserSMO().addAny(paramMap);
            if (resultKey != null && resultKey > 0) {
                log.debug("return resultKey=" + resultKey);
                returnFlag = true;
            } else {
                returnFlag = false;
            }
            return (String.valueOf(returnFlag));
        } catch (Exception e) {
            log.error("addAnyError", e);
            return (String.valueOf(false));
        }
    }

    //updateAny
    public String updateAny(String jsonString) {
        try {
            boolean returnFlag = false;
            Map<String, Object> param = (Map<String, Object>) JsonUtil.getObject(jsonString, Map.class);
            Integer updateRow = this.getUserSMO().updateAny(param);
            if (updateRow != null && updateRow > 0) {
                returnFlag = true;
            }
            return (String.valueOf(returnFlag));
        } catch (Exception e) {
            log.error("updateAnyError", e);
            throw new RuntimeException(e);
        }
    }

    //delAny
    public String delAny(String jsonString) {
        try {
            boolean returnFlag = false;
            Map<String, Object> param = (Map<String, Object>) JsonUtil.getObject(jsonString, Map.class);
            Integer delRow = this.getUserSMO().delAny(param);
            if (delRow != null && delRow > 0) {
                returnFlag = true;
            }
            return (String.valueOf(returnFlag));
        } catch (Exception e) {
            log.error("delAnyError", e);
            throw new RuntimeException(e);
        }
    }

}

