package com.commons.util;


import com.commons.spring.SpringBeanInvoker;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.manager.user.dao.impl.UserDAOImpl;

import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 17-2-23
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class ExpandHanlerUtil {
    private static UserDAOImpl impl;
    private static SqlMapClient sqlMapClient;
   static
    {
       impl = (UserDAOImpl)SpringBeanInvoker.getBean("userDAO");
       sqlMapClient=impl.getSqlMapClient();
    }
    //批处理中不提供查询业务，一次性执行的,只提供增，删，改服务
    public static void ExpandUtilTransaction(ArrayList<String> sqlmap) throws Exception{
 //       Map<String, Object> param = (Map<String, Object>) JsonUtil.getObject(jsonString, Map.class);


        try {
            sqlMapClient.startTransaction();
            sqlMapClient.startBatch();

            for(String sql:sqlmap)
            {
                Map<String, Object> param = (Map<String, Object>) JsonUtil.getObject(sql, Map.class);
                Set<String> keyset=param.keySet();
                Iterator<String> keyiterator=keyset.iterator();
                while(keyiterator.hasNext())
                {
                    String key=keyiterator.next();
                    if(key.equals("addAnySQL"))
                    {
                        sqlMapClient.insert("user.addAny", param);
                        String sqlinstance=(String)param.get(key);
                        System.out.println("addAnySQL:"+sqlinstance);
                    }
                    else if(key.equals("updateAnySQL"))
                    {
                        sqlMapClient.update("user.updateAny", param);
                        String sqlinstance=(String)param.get(key);
                        System.out.println("updateAnySQL:"+sqlinstance);
                    }
                    else if(key.equals("delAnySQL"))
                    {
                        sqlMapClient.update("user.delAny", param);
                        String sqlinstance=(String)param.get(key);
                        System.out.println("delAnySQL:"+sqlinstance);
                    }

                 }
            }

            sqlMapClient.executeBatch();
        } catch (Exception e) {
            // TODO: handle exception
        }finally{
            sqlMapClient.commitTransaction();
            sqlMapClient.endTransaction();
        }
    }

}
