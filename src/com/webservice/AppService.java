package com.webservice;

import com.app.web.user.UserHttpImpl;
import com.commons.util.JsonUtil;
import com.commons.util.StringUtil;
import com.commons.util.WeChatUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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

public class AppService extends HttpServlet {
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

        if(domain.equals("login")){
            JSONObject obj = JSONObject.fromObject(params);
            String username=obj.get("username").toString();
            String pwd=obj.get("pwd").toString();
            String platform =obj.get("platform").toString();
            ui = new UserHttpImpl();

            if(platform.equals("wxpt")){
                //微信平台登录
                String sql = "SELECT USERID,USERNAME FROM WX_USER WHERE USERNAME='"+username+"' AND PASSWORD='"+pwd+"'";
                jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                JSONArray  ja=JSONArray.fromObject(jsonStr);
                JSONObject rejson = ja.getJSONObject(0);
                String userid=rejson.get("USERID").toString();
                session.setAttribute(userid,"wxpt");
            }else if(platform.equals("store")){
                //商户登录
                String sql = "SELECT * FROM STORE_SUP_SUPPLY WHERE SUP_USERNAME='"+username+"' AND SUP_PWD='"+pwd+"'";
                jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                JSONArray  ja=JSONArray.fromObject(jsonStr);
                JSONObject rejson = ja.getJSONObject(0);
                String userid=rejson.get("SUP_ID").toString();
                session.setAttribute(userid,"store");
            }
        } else if(objs.has("user")){
            //微信平台入口
            if(session.getAttribute(objs.get("user").toString())!=null){
                if( session.getAttribute(objs.get("user").toString()).equals("wxpt")){
                    if(domain.equals("delConfigure")){
                        JSONObject obj = JSONObject.fromObject(params);
                        String id=obj.get("id").toString();
                        ui = new UserHttpImpl();
                        String sql = "DELETE FROM WX_PUBLIC WHERE id="+id+"";
                        jsonStr=ui.delAny(getJsonSql("delAnySQL",sql));
                    }
                    if(domain.equals("saveConfigure")){
                        JSONObject obj = JSONObject.fromObject(params);
                        String publicid=obj.get("public_id").toString();
                        String publicname=obj.get("public_name").toString();
                        String type=obj.get("type").toString();
                        String userid=obj.get("user").toString();
                        String wechat=obj.get("wechat").toString();
                        String id=obj.get("id").toString();
                        ui = new UserHttpImpl();
                        String sql = "INSERT INTO WX_PUBLIC (ID,USERID,PUBLICID,NAME,WECHAT,TYPE) VALUES('"+id+"','"+userid+"','"+publicid+"','"+publicname+"','"+wechat+"','"+type+"')";
                        jsonStr=ui.addAny(getJsonSql("addAnySQL",sql));
                    }
                    if(domain.equals("updateConfigure")){
                        JSONObject obj = JSONObject.fromObject(params);
                        String publicid=obj.get("public_id").toString();
                        String publicname=obj.get("public_name").toString();
                        String type=obj.get("type").toString();
                        String userid=obj.get("user").toString();
                        String wechat=obj.get("wechat").toString();
                        String id=obj.get("id").toString();
                        ui = new UserHttpImpl();
                        String sql = "UPDATE WX_PUBLIC SET publicid='"+publicid+"',name='"+publicname+"',type='"+type+"',wechat='"+wechat+"' where userid='"+userid+"' and id='"+id+"' ";
                        jsonStr=ui.updateAny(getJsonSql("updateAnySQL",sql));
                    }
                    if(domain.equals("saveConfigureSec")){
                        System.out.println(params);
                        JSONObject obj = JSONObject.fromObject(params);
                        String appID=obj.get("appID").toString();
                        String appSecret=obj.get("appSecret").toString();
                        String encodingAESKey=obj.get("encodingAESKey").toString();
                        String wxcode=obj.get("wxcode").toString();
                        String id=obj.get("id").toString();
                        ui = new UserHttpImpl();
                        StringBuffer sb=new StringBuffer();

                        if(id!=null && !id.equals("")){
                            sb.append(" and id='"+id+"'");
                        }else if(wxcode!=null && !wxcode.equals("")){
                            sb.append(" and wechat='"+wxcode+"'");
                        }else{
                            throw new ServletException();
                        }
                        String sql = "UPDATE WX_PUBLIC SET appid='"+appID+"',appSecret='"+appSecret+"',encodingAESKey='"+encodingAESKey+"' where 1=1 ";
                        jsonStr=ui.updateAny(getJsonSql("updateAnySQL",sql+sb.toString()));
                    }
                    if (domain.equals("saveMsgs")) {
                        JSONObject obj = JSONObject.fromObject(params);
                        String content=obj.get("content").toString();
                        String radiotype=obj.get("radiotype").toString();
                        String msgtype=obj.get("msgtype").toString();
                        String userid=obj.get("user").toString();
                        UUID uuid=UUID.randomUUID();
                        ui = new UserHttpImpl();
                        String sql = "INSERT INTO WX_MASSAGE (ID,USERID,CONTENT,MSGTYPE,TYPE) VALUES('"+uuid+"','"+userid+"','"+content+"','"+msgtype+"','"+radiotype+"');";
                        jsonStr=ui.addAny(getJsonSql("addAnySQL",sql));
                    }
                    if (domain.equals("saveCustomerMenu")) {
                        JSONObject obj = JSONObject.fromObject(params);
                        String title=obj.get("title").toString();
                        String id=obj.get("id").toString();
                        String parent=obj.get("parent").toString();
                        ui = new UserHttpImpl();
                        String sql = "UPDATE WP_CUSTOM_MENU SET title='"+title+"' , pid='"+parent+"' where id='"+id+"'";
                        jsonStr=ui.updateAny(getJsonSql("updateAnySQL",sql));
                    }
                    if (domain.equals("getCustomerMenu")) {
                        //                    String result= null;
                        //                    try {
                        //                        result = WeChatUtil.callWeChat("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=", "");
                        //                    } catch (Exception e) {
                        //                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        //                    }
                        //                    JSONObject obj = JSONObject.fromObject(params);
                        //                    String userid=obj.get("user").toString();
                        //                    JSONArray array = JSONArray.fromObject("["+result+"]");
                        //                    JSONObject restr = JSONObject.fromObject(array.get(0));
                        //                    JSONObject menu=restr.getJSONObject("menu");
                        //                    JSONArray buttons=JSONArray.fromObject(menu.get("button"));
                        //                    JSONArray rea=new JSONArray();
                        //                    for(int i=0;i<buttons.size();i++){
                        //                        JSONObject button=JSONObject.fromObject(buttons.get(i));
                        //                        JSONArray subbuttons = JSONArray.fromObject(button.get("sub_button"));
                        //                        JSONObject parent=new JSONObject();
                        //                        UUID uuid=UUID.randomUUID();
                        //                        ui = new UserHttpImpl();
                        //                        String type;
                        //                        if(button.has("type")){
                        //                            type=button.get("type").toString();
                        //                        }else{
                        //                            type="0";
                        //                        }
                        //                        String parentsql = "insert into wx_massage (id,parentid,type,userid,name) values('"+uuid+"','','"+type+"','"+userid+"','"+button.get("name")+"');";
                        //                        jsonStr=ui.addAny(getJsonSql("addAnySQL",parentsql));
                        //                        for(int j=0;j<subbuttons.size();j++){
                        //                            JSONObject subbutton=JSONObject.fromObject(subbuttons.get(j));
                        //                            if(!subbutton.isEmpty()){
                        //                                UUID subuuid=UUID.randomUUID();
                        //                                String subsql = "insert into wx_massage (id,parentid,type,userid,name) values('"+subuuid+"','"+uuid+"','"+subbutton.get("type")+"','"+userid+"','"+subbutton.get("name")+"');";
                        //                                jsonStr=ui.addAny(getJsonSql("addAnySQL",subsql));
                        //                            }
                        //                        }
                        //                    }
                    }
                    if (domain.equals("submitCustomerMenu")) {
                        //取出数据库数据


                        ui = new UserHttpImpl();
                        JSONObject obj = JSONObject.fromObject(params);
                        //                    String user=obj.get("user").toString();
                        //                    String id=obj.get("id").toString();
                        String sql="SELECT * FROM WX_MENU";
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                        //解析返回json
                        JSONArray ja=JSONArray.fromObject(jsonStr);
                        JSONArray parent=new JSONArray();
                        JSONArray sub=new JSONArray();
                        for(int i=0;i<ja.size();i++){
                            JSONObject jo= (JSONObject) ja.get(i);
                            if(jo.get("PARENTID").toString().equals("0")){
                                parent.add(jo);
                            }else{
                                sub.add(jo);
                            }
                        }
                        //拼接给微信的json
                        StringBuffer sb=new StringBuffer();
                        sb.append("{\"button\":[");

                        for(int i=0;i<parent.size();i++){
                            Boolean hassub=false;
                            JSONObject parents= (JSONObject) parent.get(i);
                            sb.append("{\"name\":\""+parents.get("NAME")+"\",\"type\":\""+parents.get("TYPE")+"\",\"key\":\""+parents.get("key")+"\",\"sub_button\":[");
                            for(int j=0;j<sub.size();j++){
                                JSONObject subs= (JSONObject) sub.get(j);
                                if(subs.get("PARENTID").equals(parents.get("ID"))){
                                    if(subs.get("TYPE").toString().equals("view")){
                                        sb.append("{\"type\":\""+subs.get("TYPE")+"\",\"name\":\""+subs.get("NAME")+"\",\"url\":\""+subs.get("URL")+"\",\"key\":\""+subs.get("KEY")+"\",\"sub_button\":[]},");
                                    }else{
                                        sb.append("{\"type\":\""+subs.get("TYPE")+"\",\"name\":\""+subs.get("NAME")+"\",\"key\":\""+subs.get("KEY")+"\",\"sub_button\":[]},");
                                    }
                                    hassub=true;
                                }
                            }
                            if(hassub){
                                sb.deleteCharAt(sb.length()-1);
                            }
                            sb.append("]},");
                        }
                        sb.deleteCharAt(sb.length()-1);
                        sb.append("]}");
                        //调用微信接口
                        String restr="";
                        try {
                            //先删除，再增加


                            String redel=WeChatUtil.callWeChat("https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=","");
                            String success=JSONObject.fromObject(redel).get("errmsg").toString();
                            if(success.equals("ok")){
                                restr=WeChatUtil.callWeChat("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=",sb.toString());
                            }else{
                                restr="fail";
                            }

                        } catch (Exception e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        jsonStr=restr;
                    }
                    if (domain.equals("queryCustomerMenu")) {
                        ui = new UserHttpImpl();
                        JSONObject obj = JSONObject.fromObject(params);
                        String user=obj.get("user").toString();
                        String sql = "SELECT T.ID,T.NAME,T1.NAME AS PARENTNAME,T.TYPE FROM WX_MENU T LEFT JOIN WX_MENU T1 ON T.PARENTID=T1.ID WHERE 1=1 " ;
                        //条件
                        StringBuffer sb=new StringBuffer();
                        if(obj.has("pid")){
                            sb.append(" and t.pid='"+obj.get("pid").toString()+"'");
                        }
                        if(obj.has("id")){
                            sb.append(" and t.id='"+obj.get("id").toString()+"'");
                        }
                        if(obj.has("isParent")){
                            if(obj.get("isParent").toString().equals("1")){
                                sb.append(" and t.parentid='0'");
                            }

                        }
                        sql=sql+sb.toString();
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                    }
                    if (domain.equals("queryWelcomeMsg")) {
                        System.out.println(params);
                        ui = new UserHttpImpl();
                        String sql = "SELECT * FROM WX_MESSAGE ";
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                    }
                    if(domain.equals("queryGroup")){
                        String result= null;
                        try {
                            result = WeChatUtil.callWeChat("https://api.weixin.qq.com/cgi-bin/tags/get?access_token=", "");
                        } catch (Exception e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        JSONArray array = JSONArray.fromObject("["+result+"]");
                        JSONObject restr = JSONObject.fromObject(array.get(0));
                        jsonStr=restr.get("tags").toString();
                    }
                    if(domain.equals("getFollowCount")){
                        String total="";
                        try{
                            JSONArray jao = JSONArray.fromObject("["+WeChatUtil.callWeChat("https://api.weixin.qq.com/cgi-bin/user/get?access_token=", "")+"]");
                            JSONObject jo = JSONObject.fromObject(jao.get(0));
                            total=jo.get("total").toString();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        StringBuffer sb=new StringBuffer();
                        sb.append("{\"count\":\""+total+"\"");
                        int pagec=Integer.parseInt(total)/20;
                        int pagey=Integer.parseInt(total)%20;
                        if(pagey!=0){
                            pagec+=pagec;
                        }
                        jsonStr=String.valueOf(pagec);
                    }
                    if(domain.equals("getFollowInfo")){
                        JSONObject obj = JSONObject.fromObject(params);
                        int page=Integer.parseInt(obj.get("page").toString())-1;
                        //获取列表中最后一个openid
                        List follow=new ArrayList();
                        try {
                            //查出所有用户，每次只能查出10000条数据，大于10000条需要第二次调用
                            JSONArray jao = JSONArray.fromObject("["+WeChatUtil.callWeChat("https://api.weixin.qq.com/cgi-bin/user/get?access_token=", "")+"]");
                            JSONObject jo = JSONObject.fromObject(jao.get(0));
                            JSONArray ja=jo.getJSONObject("data").getJSONArray("openid");
                            for(int i=0;i<ja.size();i++){
                                follow.add(ja.get(i));
                            }
                            String naxtopenid=jo.get("next_openid").toString();
                            String count=jo.get("count").toString();
                            //如果大于10000条，第二次调用


                            while (count.equals("10000")){
                                List rest=new ArrayList();
                                JSONObject jos = JSONObject.fromObject(WeChatUtil.callWeChat("https://api.weixin.qq.com/cgi-bin/user/get?access_token=",naxtopenid));
                                JSONArray jas=jos.getJSONObject("data").getJSONArray("openid");
                                count=jos.getJSONObject("count").toString();
                                for(int i=0;i<jas.size();i++){
                                    rest.add(jas.get(i));
                                }
                                follow.addAll(rest);
                            }
                            //拼接参数，取当前页数的20个用户


                            StringBuffer sb=new StringBuffer();
                            sb.append("{\"user_list\":[");
                            int cursize;
                            //当前页面数据如果大于20，则显示20条，如果小于20，则显示剩余条数
                            if((follow.size()-page*20)>=20){
                                cursize=20;
                            }else{
                                cursize=follow.size()-page*20;
                            }
                            for(int i=0;i<cursize;i++){
                                if(follow.get(i)!=null){
                                    sb.append("{\"openid\":\""+follow.get(i+page*20)+"\",\"lang\":\"zh-CN\"}");
                                }
                                if(i!=(cursize-1)){
                                    sb.append(",");
                                }
                            }
                            sb.append("]}");
                            String result=WeChatUtil.callWeChat("https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=",sb.toString());
                            JSONArray array = JSONArray.fromObject("["+result+"]");
                            JSONObject restr = JSONObject.fromObject(array.get(0));
                            jsonStr=restr.get("user_info_list").toString();
                        } catch (Exception e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                    }
                    if (domain.equals("register")) {
                        JSONObject obj = JSONObject.fromObject(params);
                        String username=obj.get("username").toString();
                        String realname=obj.get("realname").toString();
                        String password=obj.get("password").toString();
                        String tel=obj.get("tel").toString();
                        String address=obj.get("address").toString();
                        UUID id=UUID.randomUUID();
                        ui = new UserHttpImpl();
                        try{
                            String usersql = "INSERT INTO WX_USER (USERID,USERNAME,PASSWORD,ID) VALUES('"+id+"','"+username+"','"+password+"','"+id+"')";
                            ui.addAny(getJsonSql("addAnySQL",usersql));
                            String infosql = "INSERT INTO WX_USERINFO (USERID,REALNAME,TEL,ADDRESS) VALUES('"+id+"','"+realname+"','"+tel+"','"+address+"')";
                            ui.addAny(getJsonSql("addAnySQL",infosql));
                        }catch (Exception e){
                            jsonStr="error";
                            e.printStackTrace();;
                        }
                    }
                    if (domain.equals("queryTest")) {
                        JSONObject obj = JSONObject.fromObject(params);
                        String user;
                        String id;
                        String sql = "SELECT * FROM WX_PUBLIC WHERE 1=1";
                        StringBuffer sb=new StringBuffer();
                        if(obj.has("id")){
                            id=obj.get("id").toString() ;
                            sb.append(" and id='"+id+"'");
                        }
                        if(obj.has("user")){
                            user=obj.get("user").toString() ;
                            sb.append(" and userid='"+user+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql+sb.toString()));
                    }
                    //获取素材列表
                    if(domain.equals("getMaterial")){
                        JSONObject obj = JSONObject.fromObject(params);
                        String user=obj.get("user").toString() ;
                        int page=Integer.parseInt(obj.get("page").toString());
                        //素材类型(图片，视频，音频等)
                        String mtype=obj.get("mtype").toString();
                        String param="{\"type\":\""+mtype+"\",\"offset\":\""+page*20+"\",\"count\":\"20\"}";
                        List flist=new ArrayList();
                        try {
                            String forever=WeChatUtil.callWeChat("https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=",param);
                            JSONObject fo=JSONObject.fromObject(forever);
                            jsonStr=fo.get("item").toString();
                        } catch (Exception e) {
                            jsonStr="error";
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                    if (domain.equals("registerBasic")) {
                        JSONObject obj = JSONObject.fromObject(params);
                        String username=obj.get("username").toString();
                        String password=obj.get("password").toString();
                        UUID id=UUID.randomUUID();
                        ui = new UserHttpImpl();
                        try{
                            String usersql = "INSERT INTO WX_USER (USERID,USERNAME,PASSWORD,ID) VALUES('"+id+"','"+username+"','"+password+"','"+id+"')";
                            String addAnysql=ui.addAny(getJsonSql("addAnySQL",usersql));
                            Boolean isSuccess=Boolean.parseBoolean(addAnysql);
                            String message=null;
                            JSONObject jsonobj=new JSONObject();
                            if(isSuccess)
                            {
                                message="success";
                                jsonobj.put("newuserid",id.toString());
                                jsonobj.put("code",CODE_SUCCESS);
                            }
                            else
                            {
                                message="needRegister";
                                jsonobj.put("code",CODE_FAILURE);
                            }
                            jsonobj.put("message",message);
                            jsonStr=jsonobj.toString();
                        }catch (Exception e){
                            jsonStr="error";
                            e.printStackTrace();
                        }
                    }       //registerInfo注册用户详细信息
                    if (domain.equals("registerInfo")) {
                        JSONObject obj = JSONObject.fromObject(params);
                        String realname=obj.get("realname").toString();
                        String tel=obj.get("tel").toString();
                        String address=obj.get("address").toString();
                        String uid=obj.get("newuserid").toString();
                        ui = new UserHttpImpl();
                        try{
                            String infosql = "INSERT INTO WX_USERINFO (USERID,REALNAME,TEL,ADDRESS) VALUES('"+uid+"','"+realname+"','"+tel+"','"+address+"')";
                            String addAnysql=ui.addAny(getJsonSql("addAnySQL",infosql));
                            Boolean isSuccess=Boolean.parseBoolean(addAnysql);
                            String message=null;
                            JSONObject jsonobj=new JSONObject();
                            if(isSuccess)
                            {
                                message="success";
                                jsonobj.put("code",CODE_SUCCESS);
                            }
                            else
                            {
                                message="needRegister";
                                jsonobj.put("code",CODE_FAILURE);
                            }
                            jsonobj.put("message",message);
                            jsonStr=jsonobj.toString();
                        }catch (Exception e){
                            jsonStr="error";
                            e.printStackTrace();
                        }
                    }
                    if(domain.equals("queryAllArticle"))
                    {
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String pid=obj.getString("pid");//根据每一个公众号的token来获取所有分类下的文章


                        String sql = "SELECT CMS.ID AS ARTICLEID,CMS.KEYWORD AS ARTICLEKEYWORD,CMS.TITLE AS ARTICLETITLE,CMS.CONTENT AS ARTICLECONTENT,CATE.TITLE AS CATETITLE,CATE.CONTENT AS CATECONTENT,CATE.KEYWORD AS CATEKEYWORD FROM WX_CMS CMS,WX_CATEGORY CATE WHERE CMS.CATE_ID=CATE.ID AND CMS.CATE_ID IN (SELECT CATEG.ID FROM WX_CATEGORY CATEG WHERE categ.pid='"+pid+"')";
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                        System.out.println(jsonStr);
                    }
                    if(domain.equals("queryAllClarify"))
                    {
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String pid=obj.getString("pid");//根据每一个公众号的token来获取所有分类下的文章


                        System.out.println("pid:"+pid);
                        String sql = "SELECT * FROM WX_CATEGORY CAT WHERE cat.PID='"+pid+"'";
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("queryAllCategory"))
                    {
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String pid=obj.getString("pid");//根据每一个公众号的token来获取所有分类下的文章


                        System.out.println("pid:"+pid);
                        String sql = "SELECT * FROM WX_CATEGORY CAT WHERE cat.PID='"+pid+"'";
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("delClarify"))
                    {
                        System.out.print("dsfdsfdsfds");
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String pid=obj.getString("pid");//根据每一个公众号的token来获取所有分类下的文章


                        String uid=obj.getString("user");
                        String id=obj.getString("id");
                        System.out.println("id:"+id);
                        String sql = "DELETE FROM WX_CATEGORY WHERE id='"+id+"'";
                        Boolean isDel=Boolean.parseBoolean(ui.delAny(getJsonSql("delAnySQL",sql)));
                        System.out.println(ui.delAny(getJsonSql("delAnySQL",sql)));
                        JSONObject jsonObject=new JSONObject();
                        if(isDel)
                        {
                            jsonObject.put("code",CODE_SUCCESS);
                            jsonObject.put("message","success");
                        }
                        else
                        {
                            jsonObject.put("code",CODE_FAILURE);
                            jsonObject.put("message","failure");
                        }
                        jsonStr = jsonObject.toString();
                        System.out.println(jsonStr);
                    }
                    if(domain.equals("delArticle"))
                    {
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String uid=obj.getString("user");
                        String id=obj.getString("id");
                        System.out.println("id:"+id);
                        String sql = "DELETE FROM WX_CMS WHERE id='"+id+"'";
                        Boolean isDel=Boolean.parseBoolean(ui.delAny(getJsonSql("delAnySQL",sql)));
                        System.out.println(ui.delAny(getJsonSql("delAnySQL",sql)));
                        JSONObject jsonObject=new JSONObject();
                        if(isDel)
                        {
                            jsonObject.put("code",CODE_SUCCESS);
                            jsonObject.put("message","success");
                        }
                        else
                        {
                            jsonObject.put("code",CODE_FAILURE);
                            jsonObject.put("message","failure");
                        }
                        jsonStr = jsonObject.toString();
                        System.out.println(jsonStr);
                    }
                    if (domain.equals("saveCategory")) {
                        JSONObject obj = JSONObject.fromObject(params);
                        String title=obj.getString("categorytitle").toString();
                        String content=obj.getString("content").toString();
                        String categoryurl=obj.getString("categoryurl").toString();
                        String keyword=obj.getString("keyword").toString();
                        String pid=obj.getString("pid").toString();
                        UUID id=UUID.randomUUID();
                        ui = new UserHttpImpl();
                        try{
                            String usersql = "INSERT INTO WX_CATEGORY (ID,TITLE,URL,CONTENT,PID,KEYWORD) VALUES ('"+id+"','"+title+"','"+categoryurl+"','"+content+"','"+pid+"','"+keyword+"')";
                            String addAnysql=ui.addAny(getJsonSql("addAnySQL",usersql));
                            Boolean isSuccess=Boolean.parseBoolean(addAnysql);
                            String message=null;
                            JSONObject jsonobj=new JSONObject();
                            if(isSuccess)
                            {
                                message="success";
                                jsonobj.put("newuserid",id.toString());
                                jsonobj.put("code",CODE_SUCCESS);
                            }
                            else
                            {
                                message="needSave";
                                jsonobj.put("code",CODE_FAILURE);
                            }
                            jsonobj.put("message",message);
                            jsonStr=jsonobj.toString();
                        }catch (Exception e){
                            jsonStr="error";
                            e.printStackTrace();
                        }
                    }
                    if (domain.equals("saveArticle")) {
                        JSONObject obj = JSONObject.fromObject(params);
                        String content=obj.getString("content").toString();
                        String categorytitle=obj.getString("categorytitle").toString();
                        String categoryid=obj.getString("categoryid").toString();
                        String categorykey=obj.getString("categorykey").toString();
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateformat=format.format(new Date());
                        UUID id=UUID.randomUUID();
                        ui = new UserHttpImpl();
                        try{
                            String usersql = "INSERT INTO WX_CMS (ID,CATE_ID,KEYWORD,TITLE,CONTENT,CTIME) VALUES ('"+id+"','"+categoryid+"','"+categorykey+"','"+categorytitle+"','"+content+"',STR_TO_DATE('"+dateformat+"','%Y-%M-%D hh24:mi:ss'))";
                            String addAnysql=ui.addAny(getJsonSql("addAnySQL",usersql));
                            Boolean isSuccess=Boolean.parseBoolean(addAnysql);
                            String message=null;
                            JSONObject jsonobj=new JSONObject();
                            if(isSuccess)
                            {
                                message="success";
                                jsonobj.put("newarticleid",id.toString());
                                jsonobj.put("code",CODE_SUCCESS);
                            }
                            else
                            {
                                message="needSave";
                                jsonobj.put("code",CODE_FAILURE);
                            }
                            jsonobj.put("message",message);
                            jsonStr=jsonobj.toString();
                        }catch (Exception e){
                            jsonStr="error";
                            e.printStackTrace();
                        }
                    }
                    //上传文件
                    if(domain.equals("fileUpload")){
                        String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
                        File file = new File(savePath);
                        //判断上传文件的保存目录是否存在


                        if (!file.exists() && !file.isDirectory()) {
                            System.out.println(savePath+"目录不存在，需要创建");
                            //创建目录
                            file.mkdir();
                        }
                        //消息提示
                        String message = "";
                        try{
                            //使用Apache文件上传组件处理文件上传步骤：


                            //1、创建一个DiskFileItemFactory工厂
                            DiskFileItemFactory factory = new DiskFileItemFactory();
                            //2、创建一个文件上传解析器
                            ServletFileUpload upload = new ServletFileUpload(factory);
                            //解决上传文件名的中文乱码
                            upload.setHeaderEncoding("UTF-8");
                            //3、判断提交上来的数据是否是上传表单的数据
                            if(!ServletFileUpload.isMultipartContent(request)){
                                //按照传统方式获取数据
                                return;
                            }
                            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
                            List<FileItem> list = upload.parseRequest(request);
                            for(FileItem item : list){
                                //如果fileitem中封装的是普通输入项的数据


                                if(item.isFormField()){
                                    String name = item.getFieldName();
                                    //解决普通输入项的数据的中文乱码问题
                                    String value = item.getString("UTF-8");
                                    //value = new String(value.getBytes("iso8859-1"),"UTF-8");
                                    System.out.println(name + "=" + value);
                                }else{//如果fileitem中封装的是上传文件


                                    //得到上传的文件名称，
                                    String filename = item.getName();
                                    System.out.println(filename);
                                    if(filename==null || filename.trim().equals("")){
                                        continue;
                                    }
                                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                                    filename = filename.substring(filename.lastIndexOf("\\")+1);
                                    //获取item中的上传文件的输入流
                                    InputStream in = item.getInputStream();
                                    //创建一个文件输出流
                                    FileOutputStream fileout = new FileOutputStream(savePath + "\\" + filename);
                                    //创建一个缓冲区
                                    byte buffer[] = new byte[1024];
                                    //判断输入流中的数据是否已经读完的标识
                                    int len = 0;
                                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                                    while((len=in.read(buffer))>0){
                                        //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                                        fileout.write(buffer, 0, len);
                                    }
                                    //关闭输入流


                                    in.close();
                                    //关闭输出流


                                    fileout.close();
                                    //删除处理文件上传时生成的临时文件
                                    item.delete();
                                    message = "文件上传成功！";
                                }
                            }
                        }catch (Exception e) {
                            message= "文件上传失败！";
                            e.printStackTrace();

                        }
                    }
                }else if(session.getAttribute(objs.get("user").toString()).equals("store")){
                    //商户接口
                    String user=objs.get("user").toString();
                    if(domain.equals("goodsList"))
                    {
                        //商品列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT G_ID,G_NAME,G_PRICE,G_TYPE,G_AMOUNT,STATUS,CREATER,SUP_ID,B.DIC_NAME FROM STORE_GOODS_INFO A JOIN STORE_DIC_DICTIONARY B ON A.G_TYPE=B.DIC_CODE AND DIC_TYPE='GOODS_TYPE' WHERE sup_id='"+user+"'");
                        if(obj.has("g_id")){
                            sql.append(" and g_id='"+objs.get("g_id").toString()+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("getDicTpye"))
                    {
                        //获取字典类型，获得类型信息


                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT * FROM STORE_DIC_DICTIONARY WHERE 1=1 ");
                        if(obj.has("dic_type")){
                            sql.append(" and dic_type='"+objs.get("dic_type").toString()+"'");
                        }
                        if(obj.has("dic_parentcode")){
                            sql.append(" and dic_parentcode='"+objs.get("dic_parentcode").toString()+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("delgoods"))
                    {
                        //删除商品
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String gids=obj.getString("gids").toString();
                        String gid[]=gids.split(",");
                        for(int i=0;i<gid.length;i++){
                            String sql = "DELETE FROM STORE_GOODS_INFO WHERE g_id='"+gid[i]+"'";
                            jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                        }
                    }
                    if(domain.equals("saveOrUpdateGoods"))
                    {
                        //新增或保存商品


                        JSONObject obj = JSONObject.fromObject(params);
                        String type=obj.get("type").toString();
                        if(type.equals("saveorupdate")){
                            String userid=obj.get("user").toString();
                            String g_name=obj.get("g_name").toString();
                            String g_price=obj.get("g_price").toString();
                            String g_amount=obj.get("g_amount").toString();
                            String g_status=obj.get("g_status").toString();
                            String g_type=obj.get("g_type").toString();
                            if(obj.get("g_id").toString().length()!=32){
                                UUID uuid=UUID.randomUUID();
                                String id=uuid.toString().replaceAll("-","");
                                ui = new UserHttpImpl();
                                String sql = "INSERT INTO STORE_GOODS_INFO (G_ID,G_NAME,G_PRICE,G_TYPE,G_AMOUNT,STATUS,CREATER,SUP_ID) VALUES('"+id+"','"+g_name+"','"+g_price+"','"+g_type+"','"+g_amount+"','"+g_status+"','"+userid+"','"+userid+"')";
                                jsonStr=ui.addAny(getJsonSql("addAnySQL",sql));
                            }else{
                                String g_id=obj.get("g_id").toString();
                                String sql="UPDATE STORE_GOODS_INFO SET g_name='"+g_name+"',g_price='"+g_price+"',g_amount='"+g_amount+"',status='"+g_status+"',g_type='"+g_type+"' where g_id='"+g_id+"'" ;
                                jsonStr=ui.updateAny(getJsonSql("updateAnySQL",sql));
                            }
                        }else if(type.equals("puton")){
                            //商品上架
                            String gids=obj.getString("g_id").toString();
                            String gid[]=gids.split(",");
                            for(int i=0;i<gid.length;i++){
                                String sql = "UPDATE STORE_GOODS_INFO SET STATUS=1 WHERE g_id='"+gid[i]+"'";
                                jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                            }
                        }else if(type.equals("putoff")){
                            //商品下架
                            String gids=obj.getString("g_id").toString();
                            String gid[]=gids.split(",");
                            for(int i=0;i<gid.length;i++){
                                String sql = "UPDATE STORE_GOODS_INFO SET STATUS=0 WHERE g_id='"+gid[i]+"'";
                                jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                            }
                        }

                    }
                    if(domain.equals("discountList"))
                    {
                        //优惠卷列表


                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT S.*,(SELECT DIC_NAME FROM STORE_DIC_DICTIONARY D WHERE S.C_TYPE=D.DIC_CODE AND D.DIC_TYPE='DISCOUNT_TYPE') AS C_TYPENAME,(SELECT DIC_NAME FROM STORE_DIC_DICTIONARY D WHERE S.C_GOODTYPE=D.DIC_CODE AND D.DIC_TYPE='GOODS_TYPE') AS C_GOODSTYPENAME FROM STORE_SUP_DISCOUNT S WHERE s.sup_id='"+user+"'");
                        if(obj.has("c_id")){
                            sql.append(" and c_id='"+objs.get("c_id").toString()+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("saveOrUpdateDiscount"))
                    {
                        //新增或更新优惠卷
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String id=obj.get("id").toString();
                        String name=obj.get("name").toString();
                        String money=obj.get("money").toString();
                        String detail=obj.get("detail").toString();
                        String startmoney=obj.get("startmoney").toString();
                        String startdate=obj.get("startdate").toString();
                        String deadline=obj.get("deadline").toString();
                        String goodtype=obj.get("goodtype").toString();
                        String discounttype=obj.get("discounttype").toString();
                        if(obj.get("id").toString().length()!=32){
                            UUID uuid=UUID.randomUUID();
                            String newid=uuid.toString().replaceAll("-","");
                            ui = new UserHttpImpl();
                            String sql = "INSERT INTO STORE_SUP_DISCOUNT (C_ID,SUP_ID,C_NAME,C_MONEY,C_TYPE,C_DETAIL,C_STARTMONEY,C_STARTDATE,C_DEADLINE,C_GOODTYPE) VALUES('"+newid+"','"+user+"','"+name+"','"+money+"','"+discounttype+"','"+detail+"','"+startmoney+"',STR_TO_DATE('"+startdate+"','%Y-%M-%D'),STR_TO_DATE('"+deadline+"','%Y-%M-%D'),'"+goodtype+"')";
                            jsonStr=ui.addAny(getJsonSql("addAnySQL",sql));
                        }else{
                            String sql="UPDATE STORE_SUP_DISCOUNT SET c_name='"+name+"',c_money='"+money+"',c_type='"+discounttype+"',c_detail='"+detail+"',c_startmoney='"+startmoney+"',c_startdate=STR_TO_DATE('"+startdate+"','%Y-%M-%D'),c_deadline=STR_TO_DATE('"+deadline+"','%Y-%M-%D'),c_goodtype='"+goodtype+"'  where c_id='"+id+"' and sup_id='"+user+"'" ;
                            jsonStr=ui.updateAny(getJsonSql("updateAnySQL",sql));
                        }

                    }
                    if(domain.equals("deldiscounts"))
                    {
                        //删除优惠卷


                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String cids=obj.getString("cids").toString();
                        String cid[]=cids.split(",");
                        for(int i=0;i<cid.length;i++){
                            String sql = "DELETE FROM STORE_SUP_DISCOUNT WHERE c_id='"+cid[i]+"'";
                            jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                        }
                    }
                    if(domain.equals("commissionList")){
                        //分佣列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT A.O_ORDERID,(SELECT G_NAME FROM STORE_GOODS_INFO WHERE G_ID = A.G_ID) AS G_NAME,A.INFO_AMOUNT,INFO_TOTALPRICE,B.LASTMODIFY,(SELECT U_NAME FROM STORE_USER_INFO WHERE U_USERID IN(SELECT U_USERID FROM STORE_COMMISSION WHERE com_code = a.com_code) ) as username from store_order_details a, store_order_info b where a.g_id in (select g_id from store_goods_info where sup_id = '"+user+"')");
                        if(obj.has("orderid")&& !obj.get("orderid").toString().equals("null") && !StringUtil.isBlank(obj.get("orderid").toString())){
                            sql.append(" and a.o_orderid like '%"+objs.get("orderid").toString()+"%'");
                        }
                        if(obj.has("startDate")&& !obj.get("startDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("startDate").toString())){
                            sql.append(" and lastmodify>STR_TO_DATE('"+objs.get("startDate").toString()+"','%Y-%M-%D')");
                        }
                        if(obj.has("endDate")&& !obj.get("endDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("endDate").toString())){
                            sql.append(" and lastmodify<STR_TO_DATE('"+objs.get("endDate").toString()+"','%Y-%M-%D')");
                        }
                        if(obj.has("goodname")&& !obj.get("goodname").toString().equals("null")&& !StringUtil.isBlank(obj.get("goodname").toString())){
                            sql.append(" and a.g_id in (select g_id from store_goods_info where g_name like '%"+objs.get("goodname").toString()+"%')");
                        }
                        if(obj.has("username")&& !obj.get("username").toString().equals("null")&& !StringUtil.isBlank(obj.get("username").toString())){
                            sql.append("and a.com_code in(select a.com_code from store_order_details a, store_order_info b where b.u_userid in (select u_userid from store_user_info where u_name like '%"+obj.get("username").toString()+"%'))");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("orderDetailList")){
                        //订单明细列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT B.*,(SELECT DIC_NAME FROM STORE_DIC_DICTIONARY WHERE DIC_CODE=B.INFO_STATUS AND DIC_TYPE='INFO_STATUS') AS STATUSNAME,(SELECT G_NAME FROM STORE_GOODS_INFO WHERE G_ID=B.G_ID) AS GOODSNAME,(SELECT U_NAME FROM STORE_USER_INFO WHERE U_USERID=A.U_USERID) AS USERNAME,A.LASTMODIFY FROM STORE_ORDER_INFO A,STORE_ORDER_DETAILS B WHERE B.G_ID IN (SELECT G_ID FROM STORE_GOODS_INFO WHERE sup_id = '"+user+"')");
                        if(obj.has("infoid")&& !obj.get("infoid").toString().equals("null") && !StringUtil.isBlank(obj.get("infoid").toString())){
                            sql.append(" and b.info_id like '%"+objs.get("infoid").toString()+"%'");
                        }
                        if(obj.has("startDate")&& !obj.get("startDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("startDate").toString())){
                            sql.append(" and lastmodify>STR_TO_DATE('"+objs.get("startDate").toString()+"','%Y-%M-%D')");
                        }
                        if(obj.has("endDate")&& !obj.get("endDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("endDate").toString())){
                            sql.append(" and lastmodify<STR_TO_DATE('"+objs.get("endDate").toString()+"','%Y-%M-%D')");
                        }
                        if(obj.has("goodname")&& !obj.get("goodname").toString().equals("null")&& !StringUtil.isBlank(obj.get("goodname").toString())){
                            sql.append(" and b.g_id in (select g_id from store_goods_info where g_name like '%"+objs.get("goodname").toString()+"%')");
                        }
                        if(obj.has("username")&& !obj.get("username").toString().equals("null")&& !StringUtil.isBlank(obj.get("username").toString())){
                            sql.append("and a.u_userid in (select u_userid from store_user_info where u_userid=a.u_userid and u_name  like '%"+objs.get("username").toString()+"%')");
                        }
                        if(obj.has("status")&& !obj.get("status").toString().equals("null")&& !StringUtil.isBlank(obj.get("status").toString())){
                            sql.append("and b.info_status = '"+obj.get("status").toString()+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("approveOrder")){
                        //退款单审批
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String infoids=obj.getString("infoids").toString();
                        String infoid[]=infoids.split(",");
                        for(int i=0;i<infoid.length;i++){
                            String aprovesql = "UPDATE STORE_ORDER_DETAILS SET INFO_STATUS=7 WHERE info_id='"+infoid[i]+"'";
                            String financialsql="INSERT  INTO  STORE_ORDER_EXPENSES (SUP_ID,O_ORDERID,EXP_ID,EXP_EXPENSES,EXP_DATE,EXP_CHECKID,EXP_CHECKDATE) VALUES((SELECT SUP_ID FROM STORE_GOODS_INFO WHERE G_ID=(SELECT G_ID FROM STORE_ORDER_DETAILS WHERE info_id='"+infoid[i]+"')),'"+infoid[i]+"',sys_guid(),(select info_totalprice from store_order_details where info_id='"+infoid[i]+"'),sysdate,'"+user+"',sysdate)";
                            ui.addAny(getJsonSql("addAnySQL",financialsql));
                            jsonStr=ui.updateAny(getJsonSql("updateAnySQL",aprovesql));

                        }
                    }
                    if(domain.equals("expensesList")){
                        //支出
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT A.*,(SELECT SUP_NAME FROM STORE_SUP_SUPPLY WHERE SUP_ID=A.EXP_CHECKID) AS CHECKNAME FROM STORE_ORDER_EXPENSES A WHERE sup_id='"+user+"'");
                        if(obj.has("startDate")&& !obj.get("startDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("startDate").toString())){
                            sql.append(" and exp_date>STR_TO_DATE('"+objs.get("startDate").toString()+"','%Y-%M-%D')");
                        }
                        if(obj.has("endDate")&& !obj.get("endDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("endDate").toString())){
                            sql.append(" and exp_date<STR_TO_DATE('"+objs.get("endDate").toString()+"','%Y-%M-%D')");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("incomeList")){
                        //收入
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT * FROM STORE_ORDER_INCOME  WHERE sup_id='"+user+"'");
                        if(obj.has("startDate")&& !obj.get("startDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("startDate").toString())){
                            sql.append(" and in_date>STR_TO_DATE('"+objs.get("startDate").toString()+"','%Y-%M-%D')");
                        }
                        if(obj.has("endDate")&& !obj.get("endDate").toString().equals("null")&& !StringUtil.isBlank(obj.get("endDate").toString())){
                            sql.append(" and in_date<STR_TO_DATE('"+objs.get("endDate").toString()+"','%Y-%M-%D')");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("supMenuList")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT MENU_ID,MENU_NAME,MENU_PARENTID,MENU_CODE,MENU_URL,DATE_FORMAT(INSERTDATE,'%Y-%M-%D') AS INS,(SELECT MENU_NAME FROM STORE_SUP_MENU WHERE T.MENU_PARENTID=MENU_ID) AS MENU_PARENT FROM STORE_SUP_MENU T");
                            if(obj.has("id")){
                            sql.append(" where menu_id='"+obj.get("id")+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("saveOrUpdateSupMenu")){
                        //保存或更新页面


                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String id=obj.get("id").toString();
                        String menucode=obj.get("menucode").toString();
                        String menuname=obj.get("menuname").toString();
                        String menuurl=obj.get("menuurl").toString();
                        String parentmenu=obj.get("parentmenu").toString();
                        if(obj.get("id").toString().length()!=32){
                            ui = new UserHttpImpl();
                            String sql = "INSERT INTO STORE_SUP_MENU(MENU_ID,MENU_NAME,MENU_PARENTID,MENU_CODE,MENU_URL) VALUES (sys_guid(),'"+menuname+"','"+parentmenu+"','"+menucode+"','"+menuurl+"')";
                            jsonStr=ui.addAny(getJsonSql("addAnySQL",sql));
                        }else{
                            String sql="UPDATE STORE_SUP_MENU SET menu_name='"+menuname+"',menu_parentid='"+parentmenu+"',menu_code='"+menucode+"',menu_url='"+menuurl+"' where menu_id='"+id+"' " ;
                            jsonStr=ui.updateAny(getJsonSql("updateAnySQL",sql));
                        }
                    }
                    if(domain.equals("supplyTypeList")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT * FROM STORE_SUPPLY_TYPE WHERE sup_id='"+user+"'");
                        if(obj.has("id")){
                            String id=obj.get("id").toString();
                            sql.append(" and sup_type_id='"+id+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("saveOrUpdateSupplyType")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String id=obj.get("id").toString();
                        if(obj.get("id").toString().length()!=32){
                            String typeName=obj.get("typename").toString();
                            StringBuffer sql = new StringBuffer();
                            UUID uuid=UUID.randomUUID();
                            String type_id=uuid.toString().replaceAll("-","").toUpperCase();
                            sql.append("INSERT INTO STORE_SUPPLY_TYPE (SUP_TYPE_ID,SUP_ID,SUP_TYPE_NAME) VALUES ('"+type_id+"','"+user+"','"+typeName+"')");
                            jsonStr = ui.addAny(getJsonSql("addAnySQL", sql.toString()));
                            String menuids=obj.get("menuid").toString();
                            String[] menuid=menuids.split(",");
                            for(int i=0;i<menuid.length;i++){
                                String authsql="INSERT INTO STORE_AUTH_INFO (MENU_ID,SUP_TYPE_ID) VALUES ('"+menuid[i]+"','"+type_id+"')";
                                ui.addAny(getJsonSql("addAnySQL", authsql.toString()));
                            }
                        }else{
                            String delsql="DELETE FROM STORE_AUTH_INFO WHERE sup_type_id='"+id+"'";
                            ui.delAny(getJsonSql("delAnySQL",delsql));
                            String menuids=obj.get("menuid").toString();
                            String[] menuid=menuids.split(",");
                            for(int i=0;i<menuid.length;i++){
                                String authsql="INSERT INTO STORE_AUTH_INFO (MENU_ID,SUP_TYPE_ID) VALUES ('"+menuid[i]+"','"+id+"')";
                                ui.addAny(getJsonSql("addAnySQL", authsql.toString()));
                            }
                        }
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT * FROM STORE_SUPPLY_TYPE WHERE sup_id='"+user+"'");
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("getUserMenu")){
                        //获取用户界面
                        ui=new UserHttpImpl();
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT MENU_CODE,MENU_ID,MENU_PARENTID,MENU_NAME,MENU_URL FROM STORE_SUP_MENU WHERE 1=1 AND MENU_ID IN (SELECT MENU_ID FROM STORE_AUTH_INFO WHERE SUP_TYPE_ID IN (SELECT SUP_TYPE_ID FROM STORE_SUP_SUPPLY WHERE sup_id='"+user+"'))");
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);
                    }
                    if(domain.equals("getMenuJson")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT MENU_PARENTID,MENU_NAME,MENU_ID,MENU_URL FROM STORE_SUP_MENU WHERE MENU_ID IN (SELECT MENU_ID FROM STORE_AUTH_INFO WHERE SUP_TYPE_ID IN (SELECT SUP_TYPE_ID FROM STORE_SUP_SUPPLY WHERE sup_id='"+user+"'))");
                        String data = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));

                        Object[] ac=JsonUtil.getObjectArray(data);
                        StringBuffer sb=new StringBuffer("[");
                        for(int i=0;i<ac.length;i++){
                            JSONObject parent=JSONObject.fromObject(ac[i]);
                            if(parent.get("MENU_PARENTID").equals("null")){
                                sb.append("{\"text\": \""+parent.get("MENU_NAME")+"\",\"id\":\""+parent.get("MENU_ID")+"\"");
                                int haschild=0;
                                for(int b=0;b<ac.length;b++){
                                    JSONObject child=JSONObject.fromObject(ac[b]);
                                    if(child.get("MENU_PARENTID").equals(parent.get("MENU_ID"))){
                                        if(haschild==0){
                                            sb.append(",\"nodes\": [");
                                        }
                                        sb.append("{\"text\": \""+child.get("MENU_NAME")+"\",\"id\":\""+child.get("MENU_ID")+"\"},");
                                        haschild++;
                                    }
                                }
                                if(haschild>0){
                                    sb.deleteCharAt(sb.length()-1);
                                    sb.append("]");
                                }
                                sb.append("},");
                            }
                        }
                        sb.deleteCharAt(sb.length()-1);
                        sb.append("]");
                        System.out.println(sb.toString());
                        jsonStr=sb.toString();
                    }

                    if(domain.equals("commentList")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT (SELECT G_NAME FROM STORE_GOODS_INFO WHERE G_ID=T.G_ID) AS G_NAME,DATE_FORMAT(T.C_COMMENT) AS C_COMMENT,DATE_FORMAT(T.C_INSERTDATE,'%Y-%M-%D') AS C_INSERTDATE,U_NAME FROM STORE_ORDER_COMMENT T JOIN STORE_ORDER_INFO O ON O.O_ORDERID= T.O_ORDERID JOIN STORE_USER_INFO U ON U.U_USERID=O.U_USERID WHERE 1=1 AND G_ID IN(SELECT G_ID FROM STORE_GOODS_INFO WHERE sup_id='"+user+"')");
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                    }

                }
            }else{
                jsonStr="needlogin";
            }
        }else{
            jsonStr="needlogin";
        }

        System.out.println(jsonStr);
        out.println(jsonStr);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
