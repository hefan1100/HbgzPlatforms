package com.webservice;


import com.app.web.user.UserHttpImpl;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/1/12 0012.
 */
public class CityService extends HttpServlet{

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","POST");
        response.setHeader("Access-Control-Max-Age","60");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String domain = request.getParameter("domain") == null ? "" : request.getParameter("domain");
        HttpSession session=request.getSession();
        String jsonStr = "";
        UserHttpImpl ui=null;

        if(domain.equals("getIndexList"))//获取首页信息
        {
//            String upid=request.getParameter("upid");//省市对应下的
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            //用模糊查询效率低
            sql.append("SELECT * from store_district_dic di left join city_state_pic cs on di.id=cs.city where upid='17' order by di.id asc");
            jsonStr= ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));


            System.out.println("jsonStr:"+jsonStr);
        }
        else if(domain.equals("getIndexListByCityName"))//获取首页信息
        {
            String cityid=request.getParameter("cityid"); //城市名称
            System.out.println("城市id:"+cityid);
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
//            String articleMessage="";
            //sz表市州专区2
            sql=new StringBuffer();
            sql.append("select * from city_state_pic c inner join store_district_dic d on c.city=d.id where c.city='"+cityid+"'");
            jsonStr=ui.queryAnyList(getJsonSql("queryAnyListSQL", sql.toString()));
//            sql=new StringBuffer();
//            sql.append("select * from wx_article where SUP_ID in (" +
//                    "select sup_id from store_sup_supply where D_CITY='"+cityid+"'))" +
//                    "and type=1");
//            articleMessage= ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
//            JSONObject root=new JSONObject();
//            root.put("imginfo",JSONObject.fromObject(imgdetailmessage));
//            root.put("articlelist",JSONArray.fromObject(articleMessage));
//            jsonStr=root.toString();
            System.out.println("jsonStr:"+jsonStr);
        }
        else if(domain.equals("getLink"))
        {
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String cityid=request.getParameter("cityid");
            sql=new StringBuffer();     //武汉1 次页2
            sql.append("select c.stateurl from city_state_pic c where c.city='"+cityid+"'");
            System.out.println("sql:"+sql);
            jsonStr=ui.queryAny(getJsonSql("queryAnySQL", sql.toString()));
            System.out.println(jsonStr);
        }
        else if(domain.equals("getArticleDetails"))//获取首页信息
        {
            String cityid=request.getParameter("cityid"); //城市名称
            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();



            sql=new StringBuffer();     //武汉1 次页2
            sql.append("select * from city_state_pic c where c.city='"+cityid+"'");
            String imgdetailmessage=ui.queryAny(getJsonSql("queryAnySQL", sql.toString()));


            sql=new StringBuffer();
            sql.append("select * from wx_article w where w.SUP_ID in (select sup_id from store_sup_supply where D_CITY='"+cityid+"')  and w.TYPE=6");
            String articleMessage= ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
            JSONObject root=new JSONObject();
            root.put("imginfo",JSONObject.fromObject(imgdetailmessage));
            root.put("articlelist",JSONArray.fromObject(articleMessage));
            jsonStr=root.toString();


        }
        else if(domain.equals("getArticleDetailsInfo"))
        {
            String aid=request.getParameter("aid"); //城市名称

            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();

            sql=new StringBuffer();     //武汉1 次页2
            sql.append("SELECT * from wx_article w where w.ID='"+aid+"'");
            jsonStr= ui.queryAny(getJsonSql("queryAnySQL", sql.toString()));
        }
        System.out.println(jsonStr);
        out.println(jsonStr);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private String getJsonSql(String mathod, String sql){
        return "{\""+mathod+"\":\""+sql+"\"}";
    }
}
