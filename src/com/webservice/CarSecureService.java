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
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","POST");
        response.setHeader("Access-Control-Max-Age","60");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String domain = request.getParameter("domain") == null ? "" : request.getParameter("domain");
//        String whereSql = new String((request.getParameter("whereSql") == null ? "" : request.getParameter("whereSql")).getBytes("ISO-8859-1"),"UTF-8");
        HttpSession session=request.getSession();
        String params = new String((request.getParameter("params") == null ? "" : request.getParameter("params")));
        String jsonStr = "";
        if(domain.equals("getSecure")){
            JSONObject obj = JSONObject.fromObject(params);
            obj.get("id");
            String sql="select id,content,url,logopic,title,coverpic from wx_article where type=8";
            ui = new UserHttpImpl();
            jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
        }
        if(domain.equals("getSecureDetail")){
            JSONObject obj = JSONObject.fromObject(params);
            String sdid=obj.get("sdid").toString();
            String sql="select id,content,url,logopic,title,coverpic from wx_article where id='"+sdid+"'";
            ui = new UserHttpImpl();
            jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
        }
        if(domain.equals("SalesReg")){
            JSONObject obj = JSONObject.fromObject(params);
            String name =obj.get("name").toString();
            String province =obj.get("province").toString();
            String city =obj.get("city").toString();
            String openid=obj.get("openid").toString();
            UUID uid=UUID.randomUUID();
            String u_id=uid.toString().replaceAll("-","");
            String picurl="/upload/"+obj.get("picurl").toString();
            //先生成一个用户

            String sql="insert INTO store_user_info VALUES('"+u_id+"','','','"+openid+"','')";
            //再给用户填充信息
            UUID did=UUID.randomUUID();
            String d_id=did.toString().replaceAll("-","");
            String insertcus="insert INTO store_user_details values('"+d_id+"','"+u_id+"','"+name+"','','','"+province+"','"+city+"','','','','','','','','',sysdate(),'','1','"+picurl+"')";
            ui = new UserHttpImpl();
            jsonStr = ui.addAny(getJsonSql("addAnySQL",sql));
            if(jsonStr.equals("true")){
                ui.addAny(getJsonSql("addAnySQL",insertcus));
            }
            System.out.println(jsonStr);

        }
        if(domain.equals("cusReg")){
            JSONObject obj = JSONObject.fromObject(params);
            String name =obj.get("name").toString();
            String province =obj.get("province").toString();
            String city =obj.get("city").toString();
            String vin =obj.get("vin").toString();
            String platenum =obj.get("platenum").toString();
            String enginenum =obj.get("enginenum").toString();
            String startdate =obj.get("startDate").toString();
            String enddate =obj.get("endDate").toString();
            String openid=obj.get("openid").toString();
            UUID uid=UUID.randomUUID();
            String u_id=uid.toString().replaceAll("-","");
            //先生成一个用户

            String sql="insert INTO store_user_info VALUES('"+u_id+"','','','"+openid+"','')";
            //再给用户填充信息
            UUID did=UUID.randomUUID();
            String d_id=did.toString().replaceAll("-","");
            String insertcus="insert INTO store_user_details values('"+d_id+"','"+u_id+"','"+name+"','','','"+province+"','"+city+"','','','"+platenum+"','"+vin+"','','"+enginenum+"','"+startdate+"','"+enddate+"',sysdate(),'','2','')";
            ui = new UserHttpImpl();
            jsonStr = ui.addAny(getJsonSql("addAnySQL",sql));
            if(jsonStr.equals("true")){
                ui.addAny(getJsonSql("addAnySQL",insertcus));
            }
            System.out.println(jsonStr);
        }
        if(domain.equals("getUserList")){
            JSONObject obj = JSONObject.fromObject(params);
            String openid=obj.get("openid").toString();
            ui = new UserHttpImpl();
            StringBuffer sql=new StringBuffer();
            sql.append("SELECT D_DETAILID,U_USERID,D_NAME,D_GENDER,D_BIRTHDAY,D_PROVINCE,D_CITY,D_BRANCH,D_ADDRESS,D_PLATENUM,D_VIN,D_DRIVERLICENCE,D_ENGINENUM,D_STARTDATE,D_ENDDATE,D_INSERTDATE,MANAGER_ID,D_TYPE,D_CODEPIC,D_MOBILE,D_REMINDTIME FROM STORE_USER_DETAILS WHERE D_TYPE=2 ");
            if(StringUtil.isBlank(openid)){

            } else{
                sql.append(" AND MANAGER_ID='"+openid+"'");
            }
            jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
        }
        if(domain.equals("getMangerList")){
            JSONObject obj = JSONObject.fromObject(params);
            String openid=obj.get("openid").toString();
            ui = new UserHttpImpl();
            String sql="";
            if(StringUtil.isBlank(openid)){
                sql="SELECT D_DETAILID,U_USERID,D_NAME,D_GENDER,D_BIRTHDAY,D_PROVINCE,D_CITY,D_BRANCH,D_ADDRESS,D_PLATENUM,D_VIN,D_DRIVERLICENCE,D_ENGINENUM,D_STARTDATE,D_ENDDATE,D_INSERTDATE,MANAGER_ID,D_TYPE,D_CODEPIC,D_MOBILE,D_REMINDTIME FROM STORE_USER_DETAILS WHERE D_TYPE=1";
            } else{
                sql="SELECT D_DETAILID,U_USERID,D_NAME,D_GENDER,D_BIRTHDAY,D_PROVINCE,D_CITY,D_BRANCH,D_ADDRESS,D_PLATENUM,D_VIN,D_DRIVERLICENCE,D_ENGINENUM,D_STARTDATE,D_ENDDATE,D_INSERTDATE,MANAGER_ID,D_TYPE,D_CODEPIC,D_MOBILE,D_REMINDTIME FROM STORE_USER_DETAILS WHERE U_USERID IN (SELECT MANAGER_ID FROM STORE_USER_DETAILS WHERE U_USERID IN (SELECT U_USERID FROM STORE_USER_INFO  WHERE OPENID='"+openid+"'))";
            }
            jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
        }
        if(domain.equals("CheckReg")){
            JSONObject obj = JSONObject.fromObject(params);
            String openid=obj.get("openid").toString();
            String type=obj.get("type").toString();
            ui = new UserHttpImpl();
            StringBuffer sb=new StringBuffer("");
            sb.append("SELECT D_DETAILID,U_USERID,D_NAME,D_GENDER,D_BIRTHDAY,D_PROVINCE,D_CITY,D_BRANCH,D_ADDRESS,D_PLATENUM,D_VIN,D_DRIVERLICENCE,D_ENGINENUM,D_STARTDATE,D_ENDDATE,D_INSERTDATE,MANAGER_ID,D_TYPE,D_CODEPIC,D_MOBILE,D_REMINDTIME FROM STORE_USER_DETAILS WHERE U_USERID IN (SELECT U_USERID FROM STORE_USER_INFO WHERE 1=1 ");
            if(!StringUtil.isEmpty(openid)){
                 sb.append(" AND OPENID='"+openid+"'");
            }
            if(!StringUtil.isEmpty(type)){
                sb.append(" AND D_TYPE='"+type+"'");
            }
            sb.append(")");
            jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sb.toString()));
        }
        if(domain.equals("branchPosition")){
            ui = new UserHttpImpl();
            String sql = "SELECT * FROM BRANCH_POSITION";
            jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));

        }
        if(domain.equals("getUser")){
            JSONObject obj = JSONObject.fromObject(params);
            String userid=obj.get("userid").toString();
            ui = new UserHttpImpl();
            StringBuffer sql=new StringBuffer();
            sql.append("SELECT D_DETAILID,U_USERID,D_NAME,D_GENDER,D_BIRTHDAY,D_PROVINCE,D_CITY,D_BRANCH,D_ADDRESS,D_PLATENUM,D_VIN,D_DRIVERLICENCE,D_ENGINENUM,D_STARTDATE,D_ENDDATE,D_INSERTDATE,MANAGER_ID,D_TYPE,D_CODEPIC,D_MOBILE,D_REMINDTIME FROM STORE_USER_DETAILS WHERE D_DETAILID='"+userid+"'");
            jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
        }
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
