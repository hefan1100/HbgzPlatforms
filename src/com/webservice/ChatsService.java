package com.webservice;


import com.app.web.user.UserHttpImpl;
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
 * Created by Administrator on 2017/1/12 0012.
 */
public class ChatsService extends HttpServlet{

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

        if(domain.equals("getChatsReply"))//获取首页信息
        {
            String sendMessage=request.getParameter("send");
            int digital=Integer.parseInt(sendMessage);
            JSONObject root=new JSONObject();
            String[] replymessage=new String[]{
               "请在湖北邮政服务号微邮局菜单下，选择寄递服务",
               "请在湖北邮政服务号微服务菜单下，选择查询服务"
            };

            if(digital==1)
                root.put("reply",replymessage[0]);
            else if(digital==2)
                root.put("reply",replymessage[1]);

    //        root.put("se",sendMessage);
            jsonStr=root.toString();
            System.out.println("jsonStr:"+jsonStr);
        }
        if(domain.equals("submitForAdvise"))//获取首页信息
        {
            String name=new String(request.getParameter("name").getBytes("ISO-8859-1"), "utf-8");
            String sex=new String(request.getParameter("sex").getBytes("ISO-8859-1"), "utf-8");
            String advisecontent=new String(request.getParameter("advisecontent").getBytes("ISO-8859-1"), "utf-8");
            String ordernumber=new String(request.getParameter("ordernumber").getBytes("ISO-8859-1"), "utf-8");
            String phonenumber=new String(request.getParameter("phonenumber").getBytes("ISO-8859-1"), "utf-8");

            ui=new UserHttpImpl();
            StringBuffer sql = new StringBuffer();
            String id= UUID.randomUUID().toString();
            sql.append("insert into wx_suggest values('"+id+"','','"+advisecontent+"','"+phonenumber+"','"+name+"','"+ordernumber+"','"+sex+"');");
            String addmessage=ui.addAny(getJsonSql("addAnySQL", sql.toString()));
            if(Boolean.parseBoolean(addmessage)==true)
            {
                JSONObject root=new JSONObject();
                root.put("code","success");
                root.put("message","已成功提交");
                jsonStr=root.toString();
            }
            else
            {
                JSONObject root=new JSONObject();
                root.put("code","fail");
                root.put("message","提交失败");
                jsonStr=root.toString();
            }
            System.out.println("jsonStr:"+jsonStr);
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
