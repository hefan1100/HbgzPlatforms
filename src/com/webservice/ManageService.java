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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

public class ManageService extends HttpServlet {
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
            if(platform.equals("store")){
                //商户登录
                String sql = "SELECT * FROM STORE_SUP_SUPPLY WHERE sup_username='"+username+"' and sup_pwd='"+pwd+"'";
                jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));
                JSONArray  ja=JSONArray.fromObject(jsonStr);
                JSONObject rejson = ja.getJSONObject(0);
                String userid=rejson.get("SUP_ID").toString();
                session.setAttribute(userid,"store");
            }
        } else if(objs.has("user")){
            //微信平台入口
            if(session.getAttribute(objs.get("user").toString())!=null){
                if(session.getAttribute(objs.get("user").toString()).equals("store")){
                    //商户接口
                    String user=objs.get("user").toString();

                    if(domain.equals("goodsCount"))
                    {
                        //商品列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT COUNT(*) AS COUNT FROM STORE_GOODS_INFO A JOIN STORE_DIC_DICTIONARY B ON A.G_TYPE=B.DIC_CODE AND DIC_TYPE='GOODS_TYPE' WHERE sup_id='"+user+"'");
                        if(obj.has("g_id")){
                            sql.append(" and g_id='"+objs.get("g_id").toString()+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);    //获取list数据
                    }
                    if(domain.equals("goodsList"))
                    {
                        //商品列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        int page=Integer.parseInt(obj.get("page").toString());
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT G_ID,G_NAME,G_PRICE,G_TYPE,G_AMOUNT,STATUS,CREATER,SUP_ID,(SELECT DIC_NAME FROM STORE_DIC_DICTIONARY WHERE DIC_TYPE='GOODS_TYPE' AND DIC_CODE=T.G_TYPE) AS DICNAME FROM STORE_GOODS_INFO T WHERE 1=1 ");
                        if(obj.has("g_id")){
                            sql.append(" and g_id='"+objs.get("g_id").toString()+"'");
                        }
                        sql.append("limit "+(page-1)*20+","+page*20+"");
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
                    if(domain.equals("discountCount"))
                    {
                        //优惠卷列表



                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT COUNT(*) AS COUNT FROM STORE_SUP_DISCOUNT S WHERE s.sup_id='"+user+"'");
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
                        sql.append("SELECT A.O_ORDERID,(SELECT G_NAME FROM STORE_GOODS_INFO WHERE G_ID = A.G_ID) AS G_NAME,A.INFO_AMOUNT,INFO_TOTALPRICE,B.LASTMODIFY,(SELECT U_NAME FROM STORE_USER_INFO WHERE U_USERID IN(SELECT U_USERID FROM STORE_COMMISSION WHERE COM_CODE = A.COM_CODE) ) AS USERNAME FROM STORE_ORDER_DETAILS A, STORE_ORDER_INFO B WHERE A.G_ID IN (SELECT G_ID FROM STORE_GOODS_INFO WHERE sup_id = '"+user+"')");
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
                        sql.append("SELECT B.*,(SELECT DIC_NAME FROM STORE_DIC_DICTIONARY WHERE DIC_CODE=B.INFO_STATUS AND DIC_TYPE='INFO_STATUS') AS STATUSNAME,(SELECT G_NAME FROM STORE_GOODS_INFO WHERE G_ID=B.G_ID) AS GOODSNAME,(SELECT D_NAME FROM STORE_USER_DETAILS WHERE U_USERID=A.U_USERID) AS USERNAME,A.LASTMODIFY FROM STORE_ORDER_INFO A,STORE_ORDER_DETAILS B WHERE B.G_ID IN (SELECT G_ID FROM STORE_GOODS_INFO WHERE sup_id = '"+user+"')");
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
                    if(domain.equals("advertList")){
                        //广告区列表



                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT ID,CONTENT,TYPE,URL,LOGOPIC,TITLE,COVERPIC FROM WX_ARTICLE");
                        if(obj.has("id")){
                            sql.append(" where id='"+obj.get("id")+"'");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(jsonStr);//获取list数据
                    }
                    if(domain.equals("saveAdvert")){
                       //新增广告
                        JSONObject obj = JSONObject.fromObject(params);
                        String type=obj.get("type").toString();
                        String title=obj.get("title").toString();
                        String content=obj.get("content").toString();
                        String url=obj.get("url").toString();
                        String coverpic="/upload/"+obj.get("coverpic").toString();

                        String logopic="/upload/"+obj.get("logopic").toString();
                        UUID uuid=UUID.randomUUID();

                        String id=uuid.toString().replaceAll("-","");
                        ui = new UserHttpImpl();
                        String sql = "INSERT INTO WX_ARTICLE(ID,CONTENT,TYPE,URL,LOGOPIC,TITLE,COVERPIC) VALUES('"+id+"','"+content+"','"+type+"','"+url+"','"+logopic+"','"+title+"','"+coverpic+"')";
                        jsonStr=ui.addAny(getJsonSql("addAnySQL",sql));


                    }
                    if(domain.equals("deladvert"))
                    {
                        //删除广告
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        String id=obj.getString("id").toString();
                        String sql = "DELETE FROM WX_ARTICLE WHERE id='"+id+"'";
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql));

                    }
                    if(domain.equals("supMenuList")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        sql.append("SELECT MENU_ID,MENU_NAME,MENU_PARENTID,MENU_CODE,MENU_URL,DATE_FORMAT(INSERTDATE,'%Y-%M-%D') AS INS,(SELECT MENU_NAME FROM STORE_SUP_MENU WHERE T.MENU_PARENTID=MENU_ID) AS MENU_PARENT FROM STORE_SUP_MENU T ORDER BY MENU_PARENT");
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
                        UUID uuid=UUID.randomUUID();
                        String u_id=uuid.toString().replaceAll("-","").toUpperCase();
                        if(obj.get("id").toString().length()!=32){
                            ui = new UserHttpImpl();
                            String sql = "INSERT INTO STORE_SUP_MENU(MENU_ID,MENU_NAME,MENU_PARENTID,MENU_CODE,MENU_URL) VALUES ('"+u_id+"','"+menuname+"','"+parentmenu+"','"+menucode+"','"+menuurl+"')";
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
                        JSONObject obj=JSONObject.fromObject(params);
                        String id="";
                        if(obj.has("id")){
                            id=obj.get("id").toString();
                        }
                        if(obj.has("id")){
                            sql.append("SELECT MENU_CODE,MENU_ID,MENU_PARENTID,MENU_NAME,MENU_URL FROM STORE_SUP_MENU WHERE 1=1 AND MENU_ID IN (SELECT MENU_ID FROM STORE_AUTH_INFO WHERE sup_type_id ='"+id+"')");
                        }else if(user.equals("112314asdadas")){
                            sql.append("SELECT MENU_CODE,MENU_ID,MENU_PARENTID,MENU_NAME,MENU_URL FROM STORE_SUP_MENU") ;
                        } else{
                            sql.append("SELECT MENU_CODE,MENU_ID,MENU_PARENTID,MENU_NAME,MENU_URL FROM STORE_SUP_MENU WHERE 1=1 AND MENU_ID IN (SELECT MENU_ID FROM STORE_AUTH_INFO WHERE SUP_TYPE_ID IN (SELECT SUP_TYPE_ID FROM STORE_SUP_SUPPLY WHERE sup_id='"+user+"'))");
                        }
                        jsonStr = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));
                        System.out.println(sql.toString());
                        System.out.println(jsonStr);
                    }
                    if(domain.equals("getMenuJson")){
                        //页面列表
                        ui=new UserHttpImpl();
                        JSONObject obj=JSONObject.fromObject(params);
                        StringBuffer sql = new StringBuffer();
                        if(user.equals("112314asdadas")){
                            sql.append("SELECT MENU_PARENTID,MENU_NAME,MENU_ID,MENU_URL FROM STORE_SUP_MENU") ;
                        } else{
                            sql.append("SELECT MENU_PARENTID,MENU_NAME,MENU_ID,MENU_URL FROM STORE_SUP_MENU WHERE MENU_ID IN (SELECT MENU_ID FROM STORE_AUTH_INFO WHERE SUP_TYPE_ID IN (SELECT SUP_TYPE_ID FROM STORE_SUP_SUPPLY WHERE sup_id='"+user+"'))");
                        }
                        String data = ui.queryAnyList(getJsonSql("queryAnyListSQL",sql.toString()));

                        Object[] ac= JsonUtil.getObjectArray(data);
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
                        sql.append("SELECT (SELECT G_NAME FROM STORE_GOODS_INFO WHERE G_ID=T.G_ID) AS G_NAME,T.C_COMMENT AS C_COMMENT,DATE_FORMAT(T.C_INSERTDATE,'%Y-%M-%D') AS C_INSERTDATE,U_NAME FROM STORE_ORDER_COMMENT T JOIN STORE_ORDER_INFO O ON O.O_ORDERID= T.O_ORDERID JOIN STORE_USER_INFO U ON U.U_USERID=O.U_USERID WHERE 1=1 AND G_ID IN(SELECT G_ID FROM STORE_GOODS_INFO WHERE sup_id='"+user+"')");
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
