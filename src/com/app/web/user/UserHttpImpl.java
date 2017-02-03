package com.app.web.user;

import com.commons.spring.SpringBeanInvoker;
import com.commons.util.JsonUtil;
import com.manager.user.smo.UserSMO;
import com.sun.enterprise.resource.SetMethodAction;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
            if(resultList!=null)
            {
                for(int i=0;i<resultList.size();i++)
                {
                    HashMap result= (HashMap) resultList.get(i);
                    Set<String> ksets =result.keySet();
                    Iterator iterator=ksets.iterator();
                    while(iterator.hasNext())
                    {
                        String key=(String)iterator.next();
                        System.out.println("key:"+key);
                        if(result.get(key) instanceof java.sql.Timestamp)
                        {
                  //          java.sql.Date d= (java.sql.Date) result.get(key);              oracle版
    //                        result.remove(key);
    //                        iterator.remove();
                            java.sql.Timestamp d= (java.sql.Timestamp) result.get(key);
                            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            result.put(key,sdf.format(d));
                        }
                        if(result.get(key) instanceof java.sql.Date)
                        {
                            //          java.sql.Date d= (java.sql.Date) result.get(key);              oracle版
                            //                        result.remove(key);
                            //                        iterator.remove();
                            java.sql.Date d= (java.sql.Date) result.get(key);
                            result.put(key,new java.util.Date(d.getTime()));
                        }
                    }
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
            if(result!=null)
            {
                    Set<String> ksets =result.keySet();
                    Iterator iterator=ksets.iterator();
                    while(iterator.hasNext())
                    {
                        String key=(String)iterator.next();
                        System.out.println("key:"+key);
                        if(result.get(key) instanceof java.sql.Timestamp)
                        {
                            //          java.sql.Date d= (java.sql.Date) result.get(key);              oracle版
                            //                        result.remove(key);
                            //                        iterator.remove();
                            java.sql.Timestamp d= (java.sql.Timestamp) result.get(key);
                            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            result.put(key,sdf.format(d));
                        }
                        if(result.get(key) instanceof java.sql.Date)
                        {
                            //          java.sql.Date d= (java.sql.Date) result.get(key);              oracle版
                            //                        result.remove(key);
                            //                        iterator.remove();
                            java.sql.Date d= (java.sql.Date) result.get(key);
                            result.put(key,new java.util.Date(d.getTime()));
                        }
                    }

                    if(result.containsKey("C_COMMENT"))
                        result.remove("C_COMMENT");
            }
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
            System.out.println("addAnySQL:" + paramMap.get("addAnySQL"));
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
            System.out.println("updateAnySQL:" + param.get("updateAnySQL"));
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
            System.out.println(param.get("delAnySQL"));
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

