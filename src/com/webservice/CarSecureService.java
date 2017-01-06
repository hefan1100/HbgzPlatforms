package com.webservice;

import com.app.web.user.UserHttpImpl;
import com.commons.util.JsonUtil;
import com.commons.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by swt on 2015/10/13.
 * sql处理方式：






 * 1、列表：queryAnyListSQL
 * 2、查询返回单行：queryAnySQL
 * 3、新增：addAnySQL
 * 4、修改：updateAnySQL
 * 5、删除：delAnySQL
 * url: http://localhost:7001/serv?domain=queryLogin
 */

public class CarSecureService extends HttpServlet {
    UserHttpImpl ui = null;
    private static final int CODE_SUCCESS=          1;
    private static final int CODE_FAILURE=          2;

    private String getJsonSql(String mathod, String sql){
        return "{\""+mathod+"\":\""+sql+"\"}";
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String domain = request.getParameter("domain") == null ? "" : request.getParameter("domain");
//        String whereSql = new String((request.getParameter("whereSql") == null ? "" : request.getParameter("whereSql")).getBytes("ISO-8859-1"),"UTF-8");
        HttpSession session=request.getSession();
        String params = new String((request.getParameter("params") == null ? "" : request.getParameter("params")));
        String jsonStr = "";
        JSONObject objs = JSONObject.fromObject(params);

        if(domain.equals("getDic")){
            JSONObject obj = JSONObject.fromObject(params);
            ui = new UserHttpImpl();
            //商户登录
            String fsql = "select id,name,level,upid from store_district_dic where level=1";
            String ssql = "select id,name,level,upid from store_district_dic where level=2";
            String tsql = "select id,name,level,upid from store_district_dic where level=3";
            String fjson = ui.queryAnyList(getJsonSql("queryAnyListSQL",fsql));
            String sjson = ui.queryAnyList(getJsonSql("queryAnyListSQL",ssql));
            String tjson = ui.queryAnyList(getJsonSql("queryAnyListSQL",tsql));
            JSONArray fja=JSONArray.fromObject(fjson);
            JSONArray sja=JSONArray.fromObject(sjson);
            JSONArray tja=JSONArray.fromObject(tjson);
            StringBuffer sb=new StringBuffer();
            sb.append("[{\"data\":\"[");
            for(int f=0;f<fja.size();f++){
                JSONObject frejson = fja.getJSONObject(f);
                sb.append("{name:'"+frejson.get("name")+"',cityList:[");
                boolean second=false;
                for(int s=0;s<sja.size();s++){
                    JSONObject srejson = sja.getJSONObject(s);
                    if(frejson.get("id").equals(srejson.get("upid"))){
                        second=true;
                        sb.append("{name:'"+srejson.get("name")+"',areaList:[");
                        boolean third=false;
                        for(int t=0;t<tja.size();t++){
                            JSONObject trejson = tja.getJSONObject(t);
                             if(srejson.get("id").equals(trejson.get("upid"))){
                                 sb.append("'"+trejson.get("name")+"',");
                                 third=true;
                             }
                        }
                        if(third){
                            sb.deleteCharAt(sb.length()-1);
                        }
                        sb.append("]},");
                    }
                }
                if(second){
                    sb.deleteCharAt(sb.length()-1);
                }
                sb.append("]},");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("]\"}]");
            jsonStr=sb.toString();
            System.out.println(sb.toString());
        }

        System.out.println(jsonStr);
        out.println(jsonStr);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
